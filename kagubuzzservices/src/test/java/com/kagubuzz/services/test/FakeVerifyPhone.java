package com.kagubuzz.services.test;

import java.util.Timer;
import java.util.TimerTask;

import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.messagetemplates.systemmessages.SystemMessage;
import com.kagubuzz.services.UserAccountService;
import com.kagubuzz.services.dispatcher.MessageDispatcher;

// This is a very iffy function

public class FakeVerifyPhone {
    
    static TBLUser user;
    
    public void fakeVerify(TBLUser user1, final CRUDDAO cruddao, final MessageDispatcher dispatcher, final UserAccountService userAccountService) {
        user = user1;
        System.out.println("Fake verify started");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                user.setPhoneVerified(true);
                cruddao.update(user);
                SystemMessage verifiedPhone = new SystemMessage(user, userAccountService.getSystemUser());
                verifiedPhone.welcomePhoneVerify();
                dispatcher.sendMessage(verifiedPhone);
                System.out.println("Fake verify success");
            }
          }, 10000 * 6 * 5);
    }
}

