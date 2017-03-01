package jiadong.portListeners;

import java.io.IOException;
import java.util.ArrayList;

import jiadong.httpPort.ClientThread;
import jiadong.managers.PortListenerManager;
import jiadong.workers.PortListener;

public class HttpPortListener extends PortListener {
	private Thread listenThread;
	private ArrayList<ClientThread> clientThreadList;
	public HttpPortListener(PortListenerManager plm, Integer portNum,
			String protocolName) {
		super(plm, portNum, protocolName);
		keepListening();
	}
	
	public void keepListening(){
		//started Server Socket.
		listenThread = new Thread(new Runnable(){
			@Override
			public void run() {
				ClientThread clientThread;
				while(HttpPortListener.this.keepListen){
					try {
						//Received A Client Socket.
						client = server.accept();
						//Passing This Client Socket To A New Thread.
						clientThread = new ClientThread(client, HttpPortListener.this.portListenerManager.getAppContext(), HttpPortListener.this);
						clientThreadList.add(clientThread);
						clientThread.start();
						HttpPortListener.this.portListenerManager.getLoggingManager().log(this, "Request Acceptance Succeeded.");
					} catch (IOException e) {
						HttpPortListener.this.portListenerManager.getLoggingManager().log(this, "Port Initialization Failed. "+ e);
					}
				}
				
			}
			
		});
		listenThread.start();
	}
	public void processRawRequest(ArrayList<String> requestMessage){
		
	}
	private void processRequest(Request request){
		
	}
	@Override
	public void listenerDestructor() {
		// TODO Auto-generated method stub
	}
}
