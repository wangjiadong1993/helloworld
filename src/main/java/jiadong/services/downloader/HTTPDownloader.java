package jiadong.services.downloader;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import jiadong.services.Service;
import jiadong.utils.HttpUtil;
import jiadong.workers.Request;
import jiadong.workers.SocketWorker;

public class HTTPDownloader implements Service, Collector {
	/**
	 * The maximum amount of download threads.
	 */
	private static final int THREAD_MAX_COUNT = 50;
	/**
	 * the size of each chunk.
	 */
	private static int CHUNK_SIZE = 1024*1024*10;
	/**
	 * The output file.
	 */
	private FileOutputStream outputFile;
	/**
	 * The start position of next chunk.
	 */
	private long _download_point = 0;
	/**
	 * The start position of next writing to the file.
	 */
	private long _request_point = 0;
	/**
	 * the size of the whole file.
	 * it can also be -1, if it is not provided by the server.
	 */
	private long _data_length = -1;
	/**
	 * termination detected.
	 */
	private boolean _data_terminal_detected = false;
	/**
	 * The handler of all working threads.
	 */
	private List<HTTPDownloadThread> workerQueue = new ArrayList<>();
	/**
	 * The registry to handle all ideal working threads.
	 */
	private List<HTTPDownloadThread> idelRegistry = new ArrayList<>();
	/**
	 * The total count of the working threads.
	 */
	private Integer threadCount = 1;
	/**
	 * The generated request for downloading.
	 */
	private Request request;
	/**
	 * The queue of chunks, that have been downloaded but not persisted.
	 */
	private PriorityQueue<DownloadChunk> chunkQueue; 
	/**
	 * 
	 */
	private DownloadStatus status = DownloadStatus.NONE;
	/**
	 * Constructor
	 * @param outputFile
	 * @param threadCount
	 * @param url
	 * @throws IOException
	 */
	public HTTPDownloader(String url) throws IOException {
		this(url, -1);
	}
	public HTTPDownloader(String url, int threadCount) throws IOException{
		this(null, threadCount, url);
	}
	public HTTPDownloader(String outputFile, int threadCount, String url) throws IOException{
		
		Request testForLength = new Request(url, "HEAD");
		try {
			this._data_length = HttpUtil.getLengthUsingHeader(testForLength);
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		this.request = new Request(url, "GET");
		
		this.outputFile = new FileOutputStream(new File(outputFile == null ? this.request.getFileName() : outputFile));
		
		this.threadCount = threadCount;
		
		if(this._data_length == -1 || threadCount == -1){
			this.threadCount = -1;
			CHUNK_SIZE = -1;
		}else{
			this.threadCount = threadCount <= 1 ? 1 : (threadCount <= THREAD_MAX_COUNT ? threadCount : THREAD_MAX_COUNT);
			chunkQueue = new PriorityQueue<>(1, new Comparator<DownloadChunk>(){
				@Override
				public int compare(DownloadChunk o1, DownloadChunk o2) {
					String o1_range =  o1.request.getHeaderValueByKey("Range");
					String o2_range =  o2.request.getHeaderValueByKey("Range");
					o1_range = o1_range.substring(o1_range.indexOf('=')+1, o1_range.indexOf('-'));
					o2_range = o2_range.substring(o2_range.indexOf('=')+1, o2_range.indexOf('-'));
					return Integer.parseInt(o1_range) - Integer.parseInt(o2_range);
				}
			});
		}
	}
	private void downloadSingleThread() {
		
		
	}
	/**
	 * Main Downloading coordination code.
	 */
	public void startDownloadTask(){
		if(this.threadCount == -1 ){
			downloadSingleThread();
			return;
		}
		
		if(CHUNK_SIZE == -1){
			try {
				SocketWorker sw = new SocketWorker(this.request.getSocket());
				byte[] data = sw.readMessage();
				outputFile.write(data);
				outputFile.flush();
				outputFile.close();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		HTTPDownloadThread httpDownloadThread;
		Request req;
		for(int i=1; i<=threadCount; i++){
			req = new Request(this.request.uri, this.request.requestMethod);
			req.setHeader("Range", "Bytes="+this._request_point + "-"+(this._request_point+CHUNK_SIZE-1));
			this._request_point += CHUNK_SIZE;
			httpDownloadThread = new HTTPDownloadThread(req, this);
			httpDownloadThread.setStatus(DownloadStatus.INITIALIZED);
			workerQueue.add(httpDownloadThread);
			(new Thread(httpDownloadThread)).start();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		while(this._data_length == -1 ? (!this._data_terminal_detected) : this._data_length >= this._request_point){
			if(this.idelRegistry.isEmpty()){
				synchronized(this.idelRegistry){
					try {
						this.idelRegistry.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			httpDownloadThread = this.idelRegistry.remove(0);
			httpDownloadThread.getRequest().setHeader("Range", "Bytes="+this._request_point + "-"+(this._request_point+CHUNK_SIZE-1));
			httpDownloadThread.setStatus(DownloadStatus.INITIALIZED);
			this._request_point += CHUNK_SIZE;
			
		}
		while(true){
			synchronized(this.idelRegistry){
				if(this.idelRegistry.size() == this.threadCount){
					break;
				}
				try {
					this.idelRegistry.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		while(!this.idelRegistry.isEmpty()){
			this.idelRegistry.remove(0).setStatus(DownloadStatus.TERMINATED);
		}
		forceWriteToFile();
	}
	public DownloadStatus getStatus(){
		return this.status;
	}
	/**
	 * Try to write to file if there is chunk available to write.
	 */
	private synchronized void tryWrite(){
		if(chunkQueue.isEmpty()) return;
		Request r = chunkQueue.peek().request;
		String range = r.getHeaderValueByKey("Range");
		if(range.startsWith("Bytes="+this._download_point+"-")){
			System.out.println("Successfully Write to File at " + this._download_point);
			writeToFile(chunkQueue.poll().chunk);
			this._download_point += CHUNK_SIZE;
			try{
				System.out.println("Next peek is: "+this.chunkQueue.peek().request.getHeaderValueByKey("Range") + " Download Point : "+this._download_point);
			}catch(NullPointerException e){
				System.out.println("Completed");
			}
			tryWrite();
		}else{
			return ;
		}
	}
	/**
	 * 
	 * Write to File.
	 */
	private synchronized void writeToFile(byte[] data) {
		try {
			this.outputFile.write(data);
			this.outputFile.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Finally push all data into the file.
	 */
	private void forceWriteToFile(){
		String range;
		for(long i=this._download_point; i<this._data_length; i+=CHUNK_SIZE){
			if(this.chunkQueue.isEmpty()) {
				System.out.println("Data Empty!");
				break;
			}
			range = this.chunkQueue.peek().request.getHeaderValueByKey("Range");
			if(range.startsWith("Bytes="+i+"-")){
				this.writeToFile(this.chunkQueue.poll().chunk);
			}else{
				System.out.println("Data Lost at "+i+"!");
				break;
			}
		}
		try {
			this.outputFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Where the sub-threads push the downloaded data.
	 */
	@Override
	public synchronized void  sendData(Request r, byte[] input) {
		/*
		 *synchronized, because called from the thread 
		 *added the received chunk into the priority queue
		 */
		synchronized(this.chunkQueue){
			this.chunkQueue.add(new DownloadChunk(r, input));
		}
		/*
		 * if the received is 0, it means there is no more data, and termination detected.
		 */
		if(input.length == 0){
			this._data_terminal_detected = true;
		}	
		/*
		 *try to write the received data into the file. 
		 */
		tryWrite();
	}
	/**
	 * Where the sub-threads register themselves
	 */
	@Override
	public synchronized void register(HTTPDownloadThread downloader) {
		/*
		 * Synchronized because called from thread.
		 */
		synchronized(this.idelRegistry){
			/*
			 * add itself to the idel registry.
			 */
			idelRegistry.add(downloader);
			/*
			 * Notify the downloader, that there is a ideal thread.
			 */
			idelRegistry.notify();
		}
	}
	/**
	 * Where the sub-threads register for failure
	 * TODO: haven't been fully implemented yet.
	 */
	@Override
	public void registerForFailure(HTTPDownloadThread downloader) {
		System.out.println("Error on: " + downloader.getRequest().getHeaderValueByKey("Range"));
		(new Thread(downloader)).start();
	}
}
