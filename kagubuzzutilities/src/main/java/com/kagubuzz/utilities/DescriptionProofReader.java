package com.kagubuzz.utilities;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DescriptionProofReader {

    private static final String EMAIL_PATTERN = ".+\\@.+\\..+";
    
    private static final String PHONE_NUMBER_PATTERN =
            "(?:\\+?1[-. ]?)?\\(?([0-9]{3})\\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})";
    
    public boolean CheckForPhoneNumbers(String description) {
        
        Pattern pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
        Matcher matcher = pattern.matcher(description);

        return matcher.find();
    }

    public boolean CheckForEmail(String description) {
        
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(description);

        return matcher.find();
    }
}
