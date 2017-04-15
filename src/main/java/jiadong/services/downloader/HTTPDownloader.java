package jiadong.services.downloader;

import java.net.Socket;

import jiadong.services.Service;

public class HTTPDownloader implements Service {
	private String outputFile;
	private int threadCount = 1;
	private String url;
	private Status DownloadStatus = Status.NONE;
	public enum Status{
		NONE,
		INITIALIZED,
		DOWNLOADING,
		FINISHED,
		PAUSED,
		ERROR
	}
	private HTTPDownloader(){
		;
	}
	public HTTPDownloader(String outputFile, int threadCount, String url){
		this();
		this.outputFile = outputFile;
		this.threadCount = threadCount;
		this.url = url;
	}
	public void startDownload(){
//		Socket socket = new Socket();
	}
	public Status getStatus(){
		return this.DownloadStatus;
	}
}
