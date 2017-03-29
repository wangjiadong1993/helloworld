package jiadong.managers;

import java.util.ArrayList;

import jiadong.App;
import jiadong.workers.PortListener;
/**
 * Initialized By the App's Main Entry.
 * It will Open the Port for Port Listener.
 * It will Manage Port Listener Destruction.
 * @author wangjiadong
 *
 */
public class PortListenerManager extends Manager{
	/**
	 * The List Of Ports That Under Management.
	 */
	private ArrayList<PortListener> portListenerList;
	/**
	 * Constructor.
	 * @param app, Context
	 * Will Initialize All the Port Listeners.
	 */
	public PortListenerManager(App app) {
		super(app);
		this.portListenerList = new ArrayList<>();
		this.initializePortListeners();
	}
	/**
	 * Called From the Constructor, to Initialize the Port Listeners.
	 */
	public void initializePortListeners(){
		this.loggingManager.log(this, "Initializing HTTP PortListener On Port 8080.");
//		this.portListenerList.add(new HttpPortListener(this, 8080, "HTTP", this.app));
	}
	/**
	 * Port Listener Destruction Management.
	 */
	@Override
	public void managerDestructor() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * Port Listener Construction Management.
	 */
	@Override
	public void managerConstructor() {
		// TODO Auto-generated method stub
		
	}
}
