package com.entity;

public class Para {
	public String pname;
	public String pvalue;

	public Para(String pname, String pvalue) {
		this.pname = pname;
		this.pvalue = pvalue;
	}
	public String getName(){
		return this.pname;
		
	}
	public String getValue(){
		return this.pvalue;
		
	}
}
