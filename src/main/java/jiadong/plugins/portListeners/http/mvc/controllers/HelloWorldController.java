package jiadong.plugins.portListeners.http.mvc.controllers;

import jiadong.plugins.portListeners.http.Response;
import jiadong.plugins.portListeners.http.mvc.Controller;


public class HelloWorldController extends Controller{

	public Response getIndex(){
		return new Response(200,this.getPage("helloworld.html"), "text/html");
	}
	public Response getMsg(){
		return new Response(200,this.getPage("helloworld.html"), "text/html");
	}
}
 