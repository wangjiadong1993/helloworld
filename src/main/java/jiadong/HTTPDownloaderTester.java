package jiadong;
import java.io.FileNotFoundException;

import jiadong.services.downloader.HTTPDownloader;

public class HTTPDownloaderTester {
	public static void main(String[] args) throws FileNotFoundException{
		HTTPDownloader hd = new HTTPDownloader("hello.output", 1, "http://128.199.76.239/chapter1/task_1.zip");
		hd.startDownloadTask();
	}
}
