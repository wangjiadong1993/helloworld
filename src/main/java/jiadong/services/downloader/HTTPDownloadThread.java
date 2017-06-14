package jiadong.services.downloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import jiadong.workers.Request;

public class HTTPDownloadThread implements Runnable{
	private char[] inputByteArray;
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
		int length = -1;
		try {
			while(true){
				while(!br.ready());
				tmp = br.readLine();
				if(tmp.startsWith("Content-Length")){
					length = Integer.parseInt(tmp.substring(tmp.indexOf(" ")+1));
				}else if(tmp.length()==0){
					break;
				}else{
					continue;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(length == -1){
			socket.close();
			System.out.println("failed due to no length.");
			return;
		}
		inputByteArray = new char[length];
		int current_loc = 0;
		int out;
		while(true){
			 out = br.read();
			if(out == -1){
				break;
			}else{
				inputByteArray[current_loc++] = (char) out;
				System.out.print((current_loc+1) %1024 == 0 ? "|" : "");
			}
		}
		if(current_loc != length){
			System.out.println("ERROR, current_loc: "+current_loc);
		}else{
			System.out.println("Great, current_loc: "+current_loc);
		}
		socket.close();
		String tmp_0 =this.request.getHeaderValueByKey("Range");
		String tmp_1 = tmp_0.substring(tmp_0.indexOf('-')+1);
		tmp_0 = tmp_0.substring(tmp_0.indexOf('=')+1, tmp_0.indexOf('-'));
		System.out.println("Data Finished: " + Integer.parseInt(tmp_0)/(Integer.parseInt(tmp_1)- Integer.parseInt(tmp_0) + 1));
		collector.sendData(this.request, this.inputByteArray);
		this.status = DownloadStatus.NONE;
		collector.register(this);
	}

}
