package jiadong.services.downloader;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import jiadong.services.Service;
import jiadong.utils.HttpUtil;
import jiadong.workers.Request;

public class HTTPDownloader implements Service, Collector {
	private static final int THREAD_MAX_COUNT = 50;
	private static final int CHUNK_SIZE = 1024;
	private FileOutputStream outputFile;
	private long _download_point = 0;
	private long _request_point = 0;
	private long _data_length = -1;
	private boolean _data_terminal_detected = false;
	private List<HTTPDownloadThread> workerQueue = new ArrayList<>();
	private List<HTTPDownloadThread> idelRegistry = new ArrayList<>();
	private int threadCount = 1;
	private Request request;
	private PriorityQueue<DownloadChunk> chunkQueue; 
	private DownloadStatus status = DownloadStatus.NONE;
	public HTTPDownloader(String outputFile, int threadCount, String url) throws FileNotFoundException{
		this.outputFile = new FileOutputStream(new File(outputFile));
		this.threadCount = threadCount;
		Request testForLength = new Request(url, "HEAD");
		try {
			this._data_length = HttpUtil.getLengthUsingHeader(testForLength.host, Integer.parseInt(testForLength.port), testForLength.getCompiledRequest());
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		System.out.println("The Data Length is: " + this._data_length);
		this.request = new Request(url, "GET");
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
	public void startDownloadTask(){
		HTTPDownloadThread httpDownloadThread;
		Request req;
		for(int i=1; i<=threadCount; i++){
			req = new Request(this.request.uri, this.request.requestMethod);
			req.setHeader("Range", "Bytes="+this._request_point + "-"+(this._request_point+CHUNK_SIZE-1));
			this._request_point += CHUNK_SIZE;
			httpDownloadThread = new HTTPDownloadThread(req, this);
			workerQueue.add(httpDownloadThread);
			(new Thread(httpDownloadThread)).start();
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
			this._request_point += CHUNK_SIZE;
			(new Thread(httpDownloadThread)).start();
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
		forceWriteToFile();
	}
	public DownloadStatus getStatus(){
		return this.status;
	}
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
	private synchronized void writeToFile(byte[] data) {
		try {
			this.outputFile.write(data);
			this.outputFile.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
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
	@Override
	public synchronized void  sendData(Request r, byte[] input) {
		this.chunkQueue.add(new DownloadChunk(r, input));
		tryWrite();
	}

	@Override
	public synchronized void register(HTTPDownloadThread downloader) {
		synchronized(this.idelRegistry){
			idelRegistry.add(downloader);
			idelRegistry.notify();
		}
		
	}
	@Override
	public void registerForFailure(HTTPDownloadThread downloader) {
		System.out.println("Error on: " + downloader.getRequest().getHeaderValueByKey("Range"));
		(new Thread(downloader)).start();
	}
}
