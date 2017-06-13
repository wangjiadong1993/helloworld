package jiadong.services.downloader;


import java.util.ArrayList;
import java.util.List;

import jiadong.managers.LoggingManager;
import jiadong.services.Service;
import jiadong.workers.Request;

public class HTTPDownloader implements Service, Collector {
	private static final int THREAD_MAX_COUNT = 10;
	private static final int CHUNK_SIZE = 1024;
	private String outputFile;
	private long _download_point = 0;
	private long _data_length = 0;
	private List<HTTPDownloadThread> workerQueue;
	private List<HTTPDownloadThread> idelRegistry = new ArrayList<>();
	private int threadCount = 1;
	private Request request;
	private Status DownloadStatus = Status.NONE;
	public enum Status{
		NONE,
		INITIALIZED,
		DOWNLOADING,
		FINISHED,
		PAUSED,
		ERROR
	}
	public HTTPDownloader(String outputFile, int threadCount, String url){
		this.outputFile = outputFile;
		this.threadCount = threadCount;
		this.request = new Request(url, "GET");
		this.threadCount = threadCount <= 1 ? 1 : (threadCount <= THREAD_MAX_COUNT ? threadCount : THREAD_MAX_COUNT);
		workerQueue = new ArrayList<>();
		
		
			
	}

	public Status getStatus(){
		return this.DownloadStatus;
	}
	public void tryWrite(){
		
	}
	@Override
	public void sendData(Request r, byte[] input) {
		
	}

	@Override
	public void register(HTTPDownloadThread downloader) {
		idelRegistry.add(downloader);
	}
}
