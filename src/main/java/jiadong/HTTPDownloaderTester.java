package jiadong;

import java.io.IOException;

import jiadong.services.downloader.HTTPDownloader;

public class HTTPDownloaderTester {
	public static void main(String[] args){
		HTTPDownloader hd = new HTTPDownloader("hello.output", 1, "http://www.baidu.com");
		try {
			hd.startDownload();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
