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
	private static final int THREAD_MAX_COUNT = 10;
	private static final int CHUNK_SIZE = 1024;
	private File outputFile;
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
	public HTTPDownloader(String outputFile, int threadCount, String url){
		this.outputFile = new File(outputFile);
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
				o1_range = o1_range.substring(0, o1_range.indexOf('-'));
				o2_range = o2_range.substring(0, o2_range.indexOf('-'));
				return Integer.parseInt(o1_range) - Integer.parseInt(o2_range);
			}
		});
	}
	public void startDownloadTask(){
		HTTPDownloadThread httpDownloadThread;
		for(int i=1; i<=threadCount; i++){
			this.request.setHeader("Range", "Bytes="+this._request_point + "-"+(this._request_point+CHUNK_SIZE));
			this._request_point += CHUNK_SIZE;
			httpDownloadThread = new HTTPDownloadThread(this.request, this);
			workerQueue.add(httpDownloadThread);
			idelRegistry.add(httpDownloadThread);
		}
		while(this._data_length == -1 ? (!this._data_terminal_detected) : this._data_length >= this._request_point){
			while(this.idelRegistry.isEmpty()){
				;
			}
			HTTPDownloadThread h = this.idelRegistry.remove(0);
			this.request.setHeader("Range", "Bytes="+this._request_point + "-"+(this._request_point+CHUNK_SIZE));
			this._request_point += CHUNK_SIZE;
			h.setRequest(this.request);
			(new Thread(h)).start();
		}
	}
	public DownloadStatus getStatus(){
		return this.status;
	}
	private void tryWrite(){
		if(chunkQueue.isEmpty()) return;
		if(chunkQueue.peek().request.getHeaderValueByKey("Range").startsWith(""+this._download_point+"-")){
			writeToFile(this.outputFile, chunkQueue.poll().chunk);
			this._download_point += CHUNK_SIZE;
			tryWrite();
		}else{
			return ;
		}
	}
	private void writeToFile(File f, byte[] data) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(f);
			fos.write(data, 0, data.length);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void sendData(Request r, byte[] input) {
		if(input.length == 0){
			
		}
		this.chunkQueue.add(new DownloadChunk(r, input));
		tryWrite();
	}

	@Override
	public void register(HTTPDownloadThread downloader) {
		idelRegistry.add(downloader);
	}
}
