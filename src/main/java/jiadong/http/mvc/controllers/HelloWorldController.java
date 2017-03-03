package jiadong.http.mvc.controllers;

import jiadong.http.mvc.Controller;
import jiadong.httpPort.Response;

public class HelloWorldController extends Controller{

	public Response getIndex(){
		return new Response(200,this.getPage("helloworld.html"), "text/html");
	}
	public Response getMsg(){
		return new Response(200,this.getPage("helloworld.html"), "text/html");
	}
}
 