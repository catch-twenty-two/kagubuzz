package com.kagubuzz.controllers;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

    private static final String BUNDLE_NAME = "messages"; //$NON-NLS-1$
    private static final ClassLoader cl = Thread.currentThread().getContextClassLoader();
    
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, new Locale("en", "US"), cl);
 
    private Messages() {

    }

    public static String getString(String key) {

        try {

            return RESOURCE_BUNDLE.getString(key);
        }
        catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
