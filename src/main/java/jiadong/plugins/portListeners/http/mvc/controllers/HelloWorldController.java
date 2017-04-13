package jiadong.plugins.portListeners.http.mvc.controllers;

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
			p.find(1L);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
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
 