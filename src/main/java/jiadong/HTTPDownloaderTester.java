package jiadong;
import java.io.IOException;

import jiadong.services.downloader.HTTPDownloader;

public class HTTPDownloaderTester {
	public static void main(String[] args) throws IOException{
		HTTPDownloader hd = new HTTPDownloader("hello.zip", 1, "https://doc-0o-78-docs.googleusercontent.com/docs/securesc/2jb9mlb0ek8le8dp145tje7ktmp5vr0p/6nl7a73va37ns2sbl79paaq0plk62mo5/1497952800000/05231810071646926161/05231810071646926161/0B2JDqtZBieGxMnhnQ2FNV2ppM1U?e=download&nonce=6v02usqabu074&user=05231810071646926161&hash=pdsdphr9la07uh9iej3pgdgen4ac8qde");
		hd.startDownloadTask();
	}
}
