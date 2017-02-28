package jiadong;

import java.util.ArrayList;

import jiadong.portListeners.HttpPortListener;

public class PortListenerManager extends Manager{
	private ArrayList<PortListener> portListenerList;
	public PortListenerManager(App app) {
		super(app);
		this.portListenerList = new ArrayList<>();
		this.initializePortListeners();
	}
	public void initializePortListeners(){
		this.loggingManager.log(this, "Initializing HTTP PortListener On Port 80.");
		this.portListenerList.add(new HttpPortListener(this, 80, "HTTP"));
	}

	@Override
	public void managerDestructor() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void managerConstructor() {
		// TODO Auto-generated method stub
		
	}

}
