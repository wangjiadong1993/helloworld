package jiadong.services.downloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;


import jiadong.workers.Request;

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
	public void startDownload() throws IOException{
		Socket socket;
		OutputStream os;
		InputStream is;
		this.status = DownloadStatus.DOWNLOADING;
		try {
			socket = new Socket(this.request.host, Integer.parseInt(this.request.port));
			os = socket.getOutputStream();
			is = socket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		try {
			String tmp = this.request.getCompiledRequest();
			os.write(tmp.getBytes());
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int tmp_int = 0;
		int maxLeng = 1024;
		byte[] buffer = new byte[maxLeng];
		ArrayList<Byte>  bufferLinkedList = new ArrayList<>();
		try {
			System.out.println("=======Start==================");
			while((tmp_int = is.read(buffer)) != -1){
				System.out.println("read: " + tmp_int +" bytes.");
				for(int i=0;i<tmp_int; i++){
					bufferLinkedList.add(new Byte(buffer[i]));
				}
			}
			System.out.println("=======End==================");
		} catch (IOException e) {
			e.printStackTrace();
		}
		int i=0;
		buffer = new byte[bufferLinkedList.size()];
		for(i=0; i< bufferLinkedList.size()-3; i++){	
			if(bufferLinkedList.get(i).intValue() == ('\r'-0) && 
				bufferLinkedList.get(i+1).intValue() == ('\n'-0) &&
				bufferLinkedList.get(i+2).intValue() == ('\r'-0) &&
				bufferLinkedList.get(i+3).intValue() == ('\n'-0)){
				break;
			}
			buffer[i] = bufferLinkedList.get(i);
		}
		buffer[i+1] = bufferLinkedList.get(i+1);
		buffer[i+2] = bufferLinkedList.get(i+2);
		buffer[i+3] = bufferLinkedList.get(i+3);
		String header = new String(buffer,0, i+4 , "ISO-8859-1");
		int index = header.indexOf("Content-Range:");
		if(index != -1 && index == bufferLinkedList.size() - (i+4)){
			int j=i+4;
			buffer = new byte[bufferLinkedList.size() - (i+4)];
			while(j<bufferLinkedList.size()){
				buffer[j-i-4] = bufferLinkedList.get(j);
				j++;
			}
			System.out.println(bufferLinkedList.size() - (i+4));
			this.collector.sendData(this.request, buffer);
		}else{
//			register as bad;
		}
		socket.close();
		this.status = DownloadStatus.NONE;
		collector.register(this);
	}

}
