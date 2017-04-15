package jiadong.services.downloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import jiadong.services.Service;

public class HTTPDownloader implements Service {
	private String outputFile;
	private int threadCount = 1;
	private String url;
	private String host;
	private int port;
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
		if(url.toLowerCase().startsWith("http://")){
			url = url.substring(7);	
		}
		this.host = this.url.substring(0,  this.url.indexOf('/'));
		if(this.host.contains(":")){
			int col = this.host.indexOf(":");
			this.port = Integer.parseInt(this.host.substring(col + 1));
			this.host = this.host.substring(0, col);
		}else{
			this.port = 80;
		}
	}
	public void startDownload() throws IOException{
		Socket socket;
		OutputStream os;
		InputStream is;
		try {
			socket = new Socket(this.host, this.port);
			os = socket.getOutputStream();
			is = socket.getInputStream();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		socket.close();
	}
	public Status getStatus(){
		return this.DownloadStatus;
	}
}
