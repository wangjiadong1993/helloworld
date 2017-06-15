package jiadong.services.downloader;


import jiadong.workers.network.Request;

public interface Collector {
	void sendData(Request r, byte[] input);
	void register(HTTPDownloadThread downloader);
	void registerForFailure(HTTPDownloadThread downloader);
}
