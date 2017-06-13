package jiadong.services.downloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import jiadong.managers.LoggingManager;
import jiadong.workers.Request;

public class HTTPDownloadThread implements Runnable{
	private byte[] inputByteArray;
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
		inputByteArray = null;
		this.status = DownloadStatus.INITIALIZED;
	}
	@Override
	public void run() {
		try {
			startDownload();
		} catch (IOException e) {
			this.status = DownloadStatus.ERROR;
			e.printStackTrace();
		}
		
	}
	public void startDownload() throws IOException{
		Socket socket;
		OutputStream os;
		InputStream is;
		BufferedReader br;
		this.status = DownloadStatus.DOWNLOADING;
		try {
			socket = new Socket(this.request.host, Integer.parseInt(this.request.port));
			os = socket.getOutputStream();
			is = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		try {
			String tmp = this.request.getCompiledRequest();
			os.write(tmp.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		String tmp = null;
		int length = 0;
		try {
			while(true){
				while(!br.ready());
				tmp = br.readLine();
//				System.out.println(tmp);
				if(tmp.startsWith("Content-Length")){
					length = Integer.parseInt(tmp.substring(tmp.indexOf(" ")+1));
				}else if(tmp.equals("\r\n")){
					break;
				}else{
					continue;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		inputByteArray = new byte[length];
		is.read(inputByteArray);
		socket.close();
		collector.sendData(this.request, this.inputByteArray);
		this.status = DownloadStatus.NONE;
		this.request= null;
		collector.register(this);
	}

}
