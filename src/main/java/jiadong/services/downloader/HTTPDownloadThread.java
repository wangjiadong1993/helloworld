package jiadong.services.downloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLSocketFactory;
import jiadong.workers.network.Request;

public class HTTPDownloadThread implements Runnable {
	private Request request;
	private Collector collector;
	private DownloadStatus status = DownloadStatus.NONE;

	public HTTPDownloadThread(Request r, Collector c) {
		this.request = r;
		this.collector = c;
		this.status = DownloadStatus.INITIALIZED;
	}

	public void setRequest(Request r) {
		this.request = r;
		this.status = DownloadStatus.INITIALIZED;
	}

	public Request getRequest() {
		return this.request;
	}

	@Override
	public void run() {
		try {
			startDownload();
		} catch (IOException e) {
			this.status = DownloadStatus.ERROR;
			e.printStackTrace();
		}
	}

	public void startDownload() throws IOException {
		Socket socket;
		OutputStream os;
		InputStream is;
		this.status = DownloadStatus.DOWNLOADING;
		if (this.request.protocolType.equals("HTTP")) {
			System.setProperty("javax.net.ssl.trustStore", "clienttrust");
			SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
			socket = ssf.createSocket(this.request.host, Integer.parseInt(this.request.port));
		} else if (this.request.protocolType.equals("HTTPS")) {
			socket = new Socket(this.request.host, Integer.parseInt(this.request.port));
		} else {
			return;
		}
		os = socket.getOutputStream();
		is = socket.getInputStream();
		try {
			String tmp = this.request.getCompiledRequest();
			os.write(tmp.getBytes());
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int tmp_int = 0;
		int maxLeng = 1024;
		byte[] buffer = new byte[maxLeng];
		List<Byte> bufferLinkedList = new ArrayList<>();
		String header = null;
		int headerContentLength = -1;
		try {
			// System.out.println("=======Start==================");
			while ((tmp_int = is.read(buffer)) != -1) {

				// reading the data into arrayList;
				System.out.println(" " + System.currentTimeMillis() + " read: " + tmp_int + " bytes.");
				for (int i = 0; i < tmp_int; i++) {
					bufferLinkedList.add(new Byte(buffer[i]));
				}
				if (tmp_int != maxLeng) {
					if (header == null) {
						int tmp = getHeaderEnd(bufferLinkedList);
						if (tmp == -1) {
							continue;
						} else {
							header = this.getHeaderString(bufferLinkedList.subList(0, tmp));
							bufferLinkedList = bufferLinkedList.subList(tmp, bufferLinkedList.size());
							headerContentLength = this.getHeaderContentLength(header);
							if (headerContentLength == bufferLinkedList.size()) {
								break;
							} else {
								continue;
							}
						}
					} else {
						if (headerContentLength == bufferLinkedList.size()) {
							break;
						} else {
							continue;
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (bufferLinkedList.size() == headerContentLength) {
			this.collector.sendData(this.request, byteListToArray(bufferLinkedList));
		} else {
			// register as bad;
		}
		socket.close();
		this.status = DownloadStatus.NONE;
		collector.register(this);
	}

	private int getHeaderEnd(List<Byte> byteList) {
		for (int i = 0; i < byteList.size() - 3; i++) {
			if (byteList.get(i).intValue() == ('\r' - 0) && byteList.get(i + 1).intValue() == ('\n' - 0)
					&& byteList.get(i + 2).intValue() == ('\r' - 0) && byteList.get(i + 3).intValue() == ('\n' - 0)) {
				return i + 4;
			}
		}
		return -1;
	}

	private byte[] byteListToArray(List<Byte> byteList) {
		byte[] tmp = new byte[byteList.size()];
		for (int i = 0; i < tmp.length; i++)
			tmp[i] = byteList.get(i);
		return tmp;
	}

	private String getHeaderString(List<Byte> bufferLinkedList) {
		byte[] tmp = byteListToArray(bufferLinkedList);
		try {
			return new String(tmp, 0, tmp.length, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	private int getHeaderContentLength(String header) {
		int contentLengthIndex = header.indexOf("Content-Length:");
		String subString = header.substring(contentLengthIndex);
		subString = subString.substring(subString.indexOf(' ') + 1, subString.indexOf('\r'));
		return Integer.parseInt(subString);
	}

}
