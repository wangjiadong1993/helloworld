package jiadong.services.downloader;

import jiadong.workers.Request;

public class DownloadChunk {
	public final Request request;
	public final byte[] chunk;
	DownloadChunk(Request r, byte[] c){
		this.request = r;
		this.chunk = c;
	}
}
