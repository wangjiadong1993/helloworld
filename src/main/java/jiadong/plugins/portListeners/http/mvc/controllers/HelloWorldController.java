package jiadong.plugins.portListeners.http.mvc.controllers;

import java.util.List;

import jiadong.managers.LoggingManager;
import jiadong.plugins.portListeners.http.Request;
import jiadong.plugins.portListeners.http.Response;
import jiadong.plugins.portListeners.http.mvc.Controller;
import jiadong.plugins.portListeners.http.mvc.models.Person;


public class HelloWorldController extends Controller{

	public Response getIndex(){
		Person p = new Person();
		p._age = 18;
		p._name = "jiadong";
		try {
			List<Person> l_p = p.find(1L);
			for(Person p_t : l_p){
				LoggingManager.getInstance().log(this, "PERSON:: "+p_t.toString());
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return new Response(200,this.getPage("helloworld.html"), "text/html");
	}
	public Response getMsg(){
		return new Response(200,this.getPage("helloworld.html"), "text/html");
	}
	public Response tryPost(){
		Request tmp = this.getRequest();
		return new Response(200,this.getPage("helloworld.html"), "text/html");
	}
}
 