package com.httpconnet;



public class Parser {
	public String getreturn(String request) {
		String[] message = request.split("\n");
		String flag= message[0];
		return flag;
	}

	
}
