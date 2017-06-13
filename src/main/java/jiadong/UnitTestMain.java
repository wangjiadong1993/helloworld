package jiadong;

import java.io.IOException;

import jiadong.services.downloader.HTTPDownloader;
import jiadong.utils.FileUtil;

public class UnitTestMain {
	public static void main(String[] args){
		System.out.println("Hello World!");
		HTTPDownloader hd = new HTTPDownloader("hello_test.out",1, "http://www.baidu.com/");
		try {
			hd.startDownload();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			System.out.println(FileUtil.readBinaryFile("hello_test.out"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
