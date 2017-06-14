package jiadong.services.downloader;

import jiadong.workers.Request;

public class DownloadChunk {
	public final Request request;
	public final char[] chunk;
	DownloadChunk(Request r, char[] c){
		this.request = r;
		this.chunk = c;
	}
}
