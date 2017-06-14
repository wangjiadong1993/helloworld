package jiadong;
import java.io.IOException;

import jiadong.services.downloader.HTTPDownloader;

public class HTTPDownloaderTester {
	public static void main(String[] args) throws IOException{
		HTTPDownloader hd = new HTTPDownloader("hello.zip", 1, "http://128.199.76.239/chapter1/task_1.zip");
		hd.startDownloadTask();
	}
}
