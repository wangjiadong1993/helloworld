package jiadong.plugins.portListeners;

import java.io.IOException;
import java.util.ArrayList;

import jiadong.App;
import jiadong.managers.*;
import jiadong.plugins.portListeners.http.Request;
import jiadong.plugins.portListeners.http.ResourceWorker;
import jiadong.plugins.portListeners.http.Response;
import jiadong.plugins.portListeners.http.RoutingWorker;
import jiadong.plugins.portListeners.http.mvc.ControllerManager;
import jiadong.workers.ClientThread;
import jiadong.workers.PortListener;

public class HttpPortListener extends PortListener {
	private Thread listenThread;
	private ArrayList<ClientThread> clientThreadList;
	public HttpPortListener(String portNum,String protocolName) {
		super(portNum, protocolName);
		clientThreadList = new ArrayList<>();
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
						clientThread = new ClientThread(client, HttpPortListener.this);
						clientThreadList.add(clientThread);
						clientThread.start();
						LoggingManager.getInstance().log(this, "Request Acceptance Succeeded.");
					}catch (IOException e) {
						LoggingManager.getInstance().log(this, "Port Initialization Failed. "+ e);
					}
				}
			}
			
		});
		listenThread.start();
	}
	public Response processRawRequest(Request request){
		Response r = processRequest(request);
		r.mergeHeaders();
		return r;
	}
	private Response processRequest(Request request){
		//routing
		RoutingWorker routingWorker = RoutingWorker.getInstance();
		LoggingManager.getInstance().log(this, routingWorker.getRoutingJson().toString());
		
		//html & json
		if(request.accept.toLowerCase().contains("html")){
			String routingResult = routingWorker.getRoute(request);
			if(routingResult == null){
				return new Response(404);
			}else{
				String[] tmp = routingResult.split("#");
				if(tmp.length < 2){
					return new Response(404);
				}
				return ControllerManager.getInstance().callController(tmp[0], tmp[1]);
			}
		}else if(request.accept.toLowerCase().contains("css")){
			return ResourceWorker.getInstance().readCSSResource(request);
		}else if(request.uri.toLowerCase().contains("js")){
			return ResourceWorker.getInstance().readJSResource(request);
		}else{
			return ResourceWorker.getInstance().readMediaResource(request);
		}
	}
	@Override
	public void listenerDestructor() {
		// TODO Auto-generated method stub
	}
}
