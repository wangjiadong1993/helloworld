package jiadong.portListeners;

import jiadong.PortListener;
import jiadong.PortListenerManager;

public class HttpPortListener extends PortListener {

	public HttpPortListener(PortListenerManager plm, Integer portNum,
			String protocolName) {
		super(plm, portNum, protocolName);
	}
	@Override
	public void listenerDestructor() {
		// TODO Auto-generated method stub
	}
}
