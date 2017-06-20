package jiadong.services.downloader;

import java.io.IOException;

import jiadong.workers.Request;
import jiadong.workers.SocketWorker;

public class HTTPDownloadThread implements Runnable{
	private Request request;
	private Collector collector;
	private DownloadStatus status = DownloadStatus.NONE;
	
	public HTTPDownloadThread(Request r, Collector c){
		this.request = r;
		this.collector = c;
		this.status = DownloadStatus.INITIALIZED;
	}
	public void setRequest(Request r){
		this.request = r;
		this.status = DownloadStatus.INITIALIZED;
	}
	public Request getRequest(){return this.request;}
	@Override
	public void run() {
		try {
			startDownload();
		} catch (IOException e) {
			this.status = DownloadStatus.ERROR;
			e.printStackTrace();
		}
	}
	public void setStatus(DownloadStatus s){
		synchronized(this.status){
			this.status = s;
		}
	}
	public boolean checkStatusIfTerminated(){
		synchronized(this.status){
			return this.status == DownloadStatus.TERMINATED;
		}
	}
	public boolean checkStatusIfInitialized(){
		synchronized(this.status){
			return this.status == DownloadStatus.INITIALIZED;
		}
	}
	public void startDownload() throws IOException{
		while(!checkStatusIfTerminated()){			
			if(checkStatusIfInitialized()){
				SocketWorker sw = new SocketWorker(this.request.getSocket());
				synchronized(this.status){
					this.status = DownloadStatus.DOWNLOADING;
				}
				try {
					sw.writeToOs(this.request.getCompiledRequest());
				} catch (IOException e) {
					e.printStackTrace();
				}
				collector.sendData(this.request, sw.readMessage());
				synchronized(this.status){
					this.status = DownloadStatus.NONE;
				}
				collector.register(this);
			}
		}
	}
}
