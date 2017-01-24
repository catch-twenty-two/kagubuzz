package com.kagubuzz.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.support.URIBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class SocialSignInController extends ProviderSignInController {

    @Autowired
    public SocialSignInController(ConnectionFactoryLocator connectionFactoryLocator, 
                                  UsersConnectionRepository usersConnectionRepository,
                                  SignInAdapter signInAdapter) {
        
        super(connectionFactoryLocator, usersConnectionRepository, signInAdapter);
        
        this.setPostSignInUrl("/home/browse");
        this.setSignUpUrl("/createfromsocial");
    }
    
}
