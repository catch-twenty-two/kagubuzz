package com.kagubuzz.servlet.utilities;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * used in SecurityAjaxFilter
 * 
 * @author miso
 *
 */
public class RedirectResponseWrapper extends HttpServletResponseWrapper {   
    private String redirect;   
    
    public RedirectResponseWrapper(HttpServletResponse httpServletResponse) {   
        super(httpServletResponse);   
    }   
    
    public String getRedirect() {   
        return redirect;   
    }   
    
    @Override
    public void sendRedirect(String string) throws IOException {   
        this.redirect = string;
    }   
    
    public void sendRedirect()
    {
    	try 
    	{
			super.sendRedirect(this.redirect);
		} 
    	catch (IOException e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}   