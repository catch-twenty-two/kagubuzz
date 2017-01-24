package com.kagubuzz.exceptions;

public class EmailInUseExecption extends Exception{

    private static final long serialVersionUID = 1L;
    
    @Override
    public String getMessage() {        
        return  "That email address is already in use.";
    }
}
