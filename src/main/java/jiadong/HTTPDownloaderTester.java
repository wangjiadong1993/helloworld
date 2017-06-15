package jiadong;
import java.io.IOException;

import jiadong.services.downloader.HTTPDownloader;

public class HTTPDownloaderTester {
	public static void main(String[] args) throws IOException{
		HTTPDownloader hd = new HTTPDownloader("hello.mkv", 1, "https://doc-0o-78-docs.googleusercontent.com/docs/securesc/2jb9mlb0ek8le8dp145tje7ktmp5vr0p/v321gc38i6aj6uleln8uis1gi0tgumef/1497535200000/05231810071646926161/05231810071646926161/0B2JDqtZBieGxMnhnQ2FNV2ppM1U?e=download&nonce=h0690rs5vj0le&user=05231810071646926161&hash=2oh7madofddppt6mfvhojiutott57edl");
		hd.startDownloadTask();
	}
}
