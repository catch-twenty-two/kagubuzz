package com.kagubuzz.exceptions;

public class UnknownZipCodeExecption extends Exception{

    private static final long serialVersionUID = 1L;
    
    @Override
    public String getMessage() {        
        return  "We couldn't find your zip code.";
    }
}
