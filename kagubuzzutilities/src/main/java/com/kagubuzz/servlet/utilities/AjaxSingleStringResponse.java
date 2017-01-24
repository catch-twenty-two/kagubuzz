package com.kagubuzz.servlet.utilities;

import java.io.Serializable;
// single string
public class AjaxSingleStringResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	String response;

	public AjaxSingleStringResponse(){};

	public AjaxSingleStringResponse(String response) {
	    if(response == null) {
	        this.response = "nodata";
	        return;
	    } 
	    this.response = response; 
	}
	
	public String getResponse(){return response;}
	public void setResponse(String response){this.response = response;}
}
