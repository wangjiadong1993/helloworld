package jiadong;
import jiadong.services.downloader.HTTPDownloader;

public class HTTPDownloaderTester {
	public static void main(String[] args){
		HTTPDownloader hd = new HTTPDownloader("hello.output", 1, "http://repo.zabbix.com/zabbix/3.2/ubuntu/pool/main/z/zabbix/zabbix_3.2.1.orig.tar.gz");
		hd.startDownloadTask();
	}
	
}
