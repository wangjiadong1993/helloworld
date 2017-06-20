package jiadong;
import java.io.IOException;

import jiadong.services.downloader.HTTPDownloader;

public class HTTPDownloaderTester {
	public static void main(String[] args) throws IOException{
		HTTPDownloader hd = new HTTPDownloader("hello.zip", 8, "http://mirror.nus.edu.sg/ubuntu-ISO/16.04.2/ubuntu-16.04.2-server-amd64.iso");
		hd.startDownloadTask();
	}
}
