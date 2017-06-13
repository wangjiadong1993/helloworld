package jiadong.services.downloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import jiadong.managers.LoggingManager;
import jiadong.services.Service;
import jiadong.workers.Request;

public class HTTPDownloader implements Service {
	private String outputFile;
	private int threadCount = 1;
	private String url;
	private String host;
	private int port;
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
	}
	public void startDownload() throws IOException{
		Socket socket;
		OutputStream os;
		InputStream is;
		BufferedReader br;
<<<<<<< HEAD
		FileOutputStream fos = new FileOutputStream(new File(this.outputFile));
=======
//		OutputStreamWriter bw = new OutputStreamWriter(new FileWriter(new File(this.outputFile)));
>>>>>>> fd444ac65497f1d3f2bfa55b38e0abc44a425c31
		try {
			socket = new Socket(this.request.host, Integer.parseInt(this.request.port));
			os = socket.getOutputStream();
			is = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
		} catch (UnknownHostException e) {
			e.printStackTrace();
			fos.close();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			fos.close();
			throw e;
		}
<<<<<<< HEAD
		String r = new Request(this.url, "GET").getCompiledRequest();
		LoggingManager.getInstance().log(null, r);
		try {
			os.write(r.getBytes());
=======
		String requestString = this.request.getCompiledRequest();
		try {
			os.write(requestString.getBytes());
>>>>>>> fd444ac65497f1d3f2bfa55b38e0abc44a425c31
		} catch (IOException e) {
			e.printStackTrace();
		}
		String tmp = null;
		int length = 0;
		try {
			while(br.ready()){
				tmp = br.readLine();
				LoggingManager.getInstance().log("What", tmp);
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
		byte[] msg = new byte[length];
		is.read(msg);
		LoggingManager.getInstance().log(this, new String(msg));
		fos.write(msg);
		socket.close();
		fos.close();
	}
	public Status getStatus(){
		return this.DownloadStatus;
	}
}
