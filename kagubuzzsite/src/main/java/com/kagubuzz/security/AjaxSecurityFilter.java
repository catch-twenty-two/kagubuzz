package com.kagubuzz.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kagubuzz.servlet.utilities.RedirectResponseWrapper;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

public class AjaxSecurityFilter extends OncePerRequestFilter {
    private Map<String, String> _ajaxRequestTokens = new HashMap<String, String>();
    Logger logger = Logger.getLogger(AjaxSecurityFilter.class);


    /**
     * Configures the name/value pair of tokens that can be applied to the request header to
     * indicate the the current request is an ajax request.  These are applied to the init-params
     * of the filter configuration in the web.xml file, and have the following form:
     * <code>
     * <init-param>
     * <param-name>ajaxRequestTokens</param-name>
     * <param-value>X-Requested-With=XMLHttpRequest,DwrRequest=AJAX</param-value>
     * </init-param>
     * </code>
     * This configuration tests the request for 2 header names: 'X-Requested-With' and 'DwrRequest'.
     * If either header name exists and contains the corresponding value, this request is
     * determined to be an ajax request.
     *
     * @param ajaxRequestTokens a string containing name/value pairs separated by commas.  Each pair is
     *                          of the form: "name=value".
     */
    public void setAjaxRequestTokens(String ajaxRequestTokens) {
        if (ajaxRequestTokens != null) {
            String[] tokens = ajaxRequestTokens.split(",");
            for (String token : tokens) {
                if (token.contains("=")) {
                    String[] pairs = token.split("=");
                    if (pairs.length == 2) {
                        _ajaxRequestTokens.put(StringUtils.trimAllWhitespace(pairs[0]), StringUtils.trimAllWhitespace(pairs[1]));
                    }
                }
            }
        }
    }

    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        
        if (!isAjaxRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

       // logger.info("AjaxSecurityFilter: Processing an AJAX call : " + request.getRequestURL());
        
        RedirectResponseWrapper redirectResponseWrapper = new RedirectResponseWrapper(response);

        // call the rest of the filters on the request
        
        filterChain.doFilter(request, redirectResponseWrapper);

        // if there was a redirect from another filter (spring security) 
        // user is probably not logged in or the log in failed.
        
		if (redirectResponseWrapper.getRedirect() == null) return;
			
		String redirectURL = redirectResponseWrapper.getRedirect();

		// Build a JSON response
		
		Map<String,Object> loginResult = new HashMap<String,Object>();
		ObjectMapper mapper = new ObjectMapper(); 
		
		response.setContentType("application/json");
		
		// look at the redirection and send a json response instead
		// this leaves it up to the jquery client to see whether it got
		// a redirect rather than the browser 
		
		if (!redirectURL.contains("loginfailed")) {
		    
		    String redirect =  redirectResponseWrapper.getRedirect();
		    
		    if(redirectURL.contains("home")) {
		        redirect = "/home/browse";
		    }
			
		    loginResult.put("redirect", redirect);
		}
		
		mapper.writeValue(response.getWriter(), loginResult);
    }


    /**
     * @param request the request object
     * @return true if this request is an ajax request.  This is determined by a configured
     *         name/value pair that is applied to the request header
     */
    protected boolean isAjaxRequest(HttpServletRequest request) {
    	
        Set<String> keys = _ajaxRequestTokens.keySet();
        
        for (String key : keys) 
        {
            String value = _ajaxRequestTokens.get(key);
            
            if (value.equalsIgnoreCase(request.getHeader(key.toLowerCase()))) 
            {
            	return true;
            }
        }
        
        return false;
    }


}