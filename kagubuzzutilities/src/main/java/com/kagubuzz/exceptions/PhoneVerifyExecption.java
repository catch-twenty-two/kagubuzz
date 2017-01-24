package com.kagubuzz.exceptions;

public class PhoneVerifyExecption extends Exception{

    private static final long serialVersionUID = 1L;
    
    @Override
    public String getMessage() {        
        return  "You have not verified your phone number yet. Please verify your number!";
    }
}
