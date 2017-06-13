package jiadong;

import java.io.IOException;

import jiadong.services.downloader.HTTPDownloader;
import jiadong.utils.FileUtil;

public class UnitTestMain {
	public static void main(String[] args){
		HTTPDownloader hd = new HTTPDownloader("hello.output", 1, "http://repo.zabbix.com/zabbix/3.2/ubuntu/pool/main/z/zabbix/zabbix_3.2.1.orig.tar.gz");
		hd.startDownloadTask();
		try {
			System.out.println(FileUtil.readBinaryFile("hello_test.out"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
