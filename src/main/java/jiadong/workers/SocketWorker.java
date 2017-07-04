package jiadong.workers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import jiadong.managers.LoggingManager;

public class SocketWorker {
	private Socket socket;
	private InputStream is;
	private OutputStream os;

	public SocketWorker(Socket socket) throws IOException {
		this.socket = socket;
		getIS();
		getOS();
	}

	public InputStream getIS() throws IOException {
		if (this.is == null) {
			this.is = this.socket.getInputStream();
		}
		return this.is;
	}

	public OutputStream getOS() throws IOException {
		if (this.os == null) {
			this.os = this.socket.getOutputStream();
		}
		return this.os;
	}

	public SocketWorker writeToOs(byte[] data, int offset, int length)
			throws IOException {
		this.os.write(data, offset, length);
		this.os.flush();
		return this;
	}

	public SocketWorker writeToOs(byte[] data) throws IOException {
		return this.writeToOs(data, 0, data.length);
	}

	public SocketWorker writeToOs(String data) throws IOException {
		return this.writeToOs(data.getBytes());
	}

	public void close() throws IOException {
		this.is.close();
		this.os.close();
		this.socket.close();
	}

	/*
	 * With \r\n
	 */
	public String readHeader() throws IOException {
		byte[] tmp = (byte[]) startDownload(true, false);
		this.close();
		if (tmp == null) {
			throw (new IOException());
		} else {
			try {
				return new String(tmp, 0, tmp.length, "ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				throw (new IOException());
			}
		}
	}

	public Response getWholeResponse() throws IOException {
		return (Response) startDownload(false, true);
	}

	public long getContentLength() throws IOException {
		String header = readHeader();
		return this.getHeaderContentLength(header);
	}

	public byte[] readMessage() throws IOException {
		return (byte[]) startDownload(false, false);
	}
	/**
	 * A downloading thread
	 * @param headerOnly
	 * @param withHeader
	 * @return
	 * @throws IOException
	 */
	private Object startDownload(boolean headerOnly, boolean withHeader)
			throws IOException {
		/**
		 * To store the length of received bytes from buffer
		 */
		int tmp_int = 0;
		/**
		 * The maximum length the buffer can store
		 */
		int maxLeng = 1024*1024*10;
		/**
		 * Buffer
		 */
		byte[] buffer = new byte[maxLeng];
		/**
		 * Counter
		 */
		int counter =0;
		/**
		 * Container for received data
		 */
		List<Byte> bufferLinkedList = new ArrayList<>();
		/**
		 * The header read from inputStream
		 */
		String header = null;
		/**
		 * Content-Length read from the header
		 */
		int headerContentLength = -1;
		try {
			/**
			 * Read into buffer, and only when it is -1 / 0, it stops.
			 * Otherwise, keep looping.
			 */
			while ((tmp_int = is.read(buffer)) > 0) {
//				/**
//				 * Stop for a while
//				 */
//				Thread.sleep(1);
//				
				/**
				 * Counter Increment
				 */
				counter ++;
				/**
				 * Read all bytes from buffer into bufferedLinkedList
				 */
				for (int i = 0; i < tmp_int; i++)
					bufferLinkedList.add(new Byte(buffer[i]));
				/**
				 * If the received bytes hasn't reach max. 
				 */
				if (tmp_int != maxLeng) {
					/**
					 * If Header is null, which means /r/n hasn't reach yet.
					 */
					if (header == null) {
						int tmp = getHeaderEnd(bufferLinkedList);
						/**
						 * Try to find the /r/n
						 */
						if (tmp == -1) {
							continue;	
						/**
						 * If found
						 */
						} else {
							/**
							 * If header-only
							 */
							if (headerOnly)
								return byteListToArray(bufferLinkedList
										.subList(0, tmp));
							/**
							 * Get the Header
							 */
							header = this.getHeaderString(bufferLinkedList
									.subList(0, tmp));
							/**
							 * Truncate the linked-list to get the body
							 */
							bufferLinkedList = bufferLinkedList.subList(tmp,
									bufferLinkedList.size());
							/**
							 * get the content-length from the header.
							 */
							headerContentLength = this
									.getHeaderContentLength(header);
							if (headerContentLength == bufferLinkedList.size()) {break;} else {continue;}
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
		if (bufferLinkedList.size() == headerContentLength
				|| headerContentLength == -1) {
			LoggingManager.getInstance().log(this, "Size is: " + bufferLinkedList.size());
			if (withHeader) {
				return new Response(header, byteListToArray(bufferLinkedList));
			} else {
				return byteListToArray(bufferLinkedList);
			}
		} else {
			LoggingManager.getInstance().log(this,
					String.valueOf(bufferLinkedList.size()));
			LoggingManager.getInstance().log(this, header);
			LoggingManager.getInstance().log(this, String.valueOf(tmp_int));
			LoggingManager.getInstance().log(this, String.valueOf(counter));
			throw (new IOException());
		}
	}
	private int getHeaderEnd(List<Byte> byteList) {
		for (int i = 0; i < byteList.size() - 3; i++) {
			if (byteList.get(i).intValue() == ('\r' - 0)
					&& byteList.get(i + 1).intValue() == ('\n' - 0)
					&& byteList.get(i + 2).intValue() == ('\r' - 0)
					&& byteList.get(i + 3).intValue() == ('\n' - 0)) {
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
		if (contentLengthIndex == -1) {
			return -1;
		} else {
			String subString = header.substring(contentLengthIndex);
			subString = subString.substring(subString.indexOf(' ') + 1,
					subString.indexOf('\r'));
			return Integer.parseInt(subString);
		}

	}
}
