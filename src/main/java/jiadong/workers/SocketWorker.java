package jiadong.workers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketWorker {
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	public SocketWorker(Socket socket) throws IOException{
		this.socket = socket;
		getIS();
		getOS();
	}
	public InputStream getIS() throws IOException{
		if(this.is == null){
			this.is = this.socket.getInputStream();
		}
		return this.is;
	}
	public OutputStream getOS() throws IOException{
		if(this.os == null){
			this.os = this.socket.getOutputStream();
		}
		return this.os;
	}
	public SocketWorker writeToOs(byte[] data, int offset, int length) throws IOException {
		this.os.write(data, offset, length);
		this.os.flush();
		return this;
	}
	public SocketWorker writeToOs(byte[] data) throws IOException{
		return this.writeToOs(data, 0, data.length);
	}
	public SocketWorker writeToOs(String data) throws IOException{
		return this.writeToOs(data.getBytes());
	}
	public void close() throws IOException{
		this.is.close();
		this.os.close();
		this.socket.close();
	}
	/*
	 * With \r\n
	 */
	public String readHeader() throws IOException{
		byte[] tmp = (byte[]) startDownload(true, false);
		this.close();
		if(tmp == null){
			throw (new IOException());
		}else{
			try {
				return new String(tmp,0, tmp.length , "ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				throw (new IOException());
			}
		}
	}
	public Response getWholeResponse() throws IOException{
		return (Response) startDownload(false, true);
	}
	public long getContentLength() throws IOException{
		String header = readHeader();
		return this.getHeaderContentLength(header);
	}
	public byte[] readMessage() throws IOException{
		return (byte[]) startDownload(false,false);
	}
	private Object startDownload(boolean headerOnly, boolean withHeader) throws IOException{
		int tmp_int = 0;
		int maxLeng = 1024;
		byte[] buffer = new byte[maxLeng];
		List<Byte>  bufferLinkedList = new ArrayList<>();
		String header = null;
		int headerContentLength=-1;
		try {
			while((tmp_int = is.read(buffer)) != -1){
				for(int i=0;i<tmp_int; i++)
					bufferLinkedList.add(new Byte(buffer[i]));
				if(tmp_int != maxLeng){
					if(header == null){
						int tmp = getHeaderEnd(bufferLinkedList);
						if(tmp == -1){
							continue;
						}else{
							if(headerOnly)
								return byteListToArray(bufferLinkedList.subList(0, tmp));
							header = this.getHeaderString(bufferLinkedList.subList(0, tmp));
							bufferLinkedList = bufferLinkedList.subList(tmp, bufferLinkedList.size());
							headerContentLength = this.getHeaderContentLength(header);
							if(headerContentLength == bufferLinkedList.size()){
								break;
							}else{
								continue;
							}
						}
					}else{
						if(headerContentLength == bufferLinkedList.size()){
							break;
						}else{
							continue;
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(bufferLinkedList.size() == headerContentLength){
			if(withHeader){
				return new Response(header, byteListToArray(bufferLinkedList));
			}else{
				return byteListToArray(bufferLinkedList);
			}
		}else{
			throw (new IOException());
		}
	}
	private int getHeaderEnd(List<Byte>  byteList){
		for(int i=0; i< byteList.size()-3; i++){	
			if(byteList.get(i).intValue() == ('\r'-0) && 
				byteList.get(i+1).intValue() == ('\n'-0) &&
				byteList.get(i+2).intValue() == ('\r'-0) &&
				byteList.get(i+3).intValue() == ('\n'-0)){
				return i+4;
			}
		}
		return -1;
	}
	private byte[] byteListToArray(List<Byte>  byteList){
		byte[] tmp = new byte[byteList.size()];
		for(int i=0; i<tmp.length; i++)
			tmp[i] = byteList.get(i);
		return tmp;
	}
	private String getHeaderString(List<Byte>  bufferLinkedList){
		byte[] tmp = byteListToArray(bufferLinkedList);
		try {
			return new String(tmp,0, tmp.length , "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	private int getHeaderContentLength(String header){
		int contentLengthIndex = header.indexOf("Content-Length:");
		if(contentLengthIndex == -1){
			return -1;
		}else{
			String subString = header.substring(contentLengthIndex);
			subString = subString.substring(subString.indexOf(' ')+1, subString.indexOf('\r'));
			return Integer.parseInt(subString);
		}
		
	}
}
