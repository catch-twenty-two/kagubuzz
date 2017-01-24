package com.kagubuzz.spring.utilities;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.exceptions.UnknownZipCodeExecption;
import com.kagubuzz.services.UserAccountService;
import com.kagubuzz.services.UserSessionService;

/**
 * Add the current version under name {@link #VERSION_MODEL_ATTRIBUTE_NAME} to
 * each model.
 * 
 * @author Ralph
 */

public class KaguUserHandlerInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private UserAccountService userAccountService;
    
    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) throws Exception {

        Cookie cookies[] = request.getCookies();

        TBLUser user = userSessionService.getUser();

        if (user.getTimeZoneOffset() == null) {

            String timeZone = getCookieValue("time_zone", cookies);

            if (timeZone != null) {
                try {
                    userSessionService.getUser().setTimeZoneOffset(Integer.parseInt(timeZone));
                }
                catch (NumberFormatException e) {

                }
            }
        }

        if (user.getTblKaguLocation() == null) {

            String zipCode = getCookieValue("zip_code", cookies);

            if (zipCode != null && !zipCode.equals("NA")) {
                try {
                    userAccountService.updateUser(user,
                            null, 
                            null, 
                            null, 
                            null, 
                            null,
                            zipCode,
                            null, 
                            user.isSocialAccount(), 
                            null, 
                            null,
                            null,
                            null,
                            null,
                            null);
                }
                catch (UnknownZipCodeExecption e) {
                    setCookieValue(cookies, "zip_code", "NA");
                }
            }
        }

        if (modelAndView != null && modelAndView.getModelMap() != null) {
            
            if(modelAndView.getModelMap().containsKey("skip_intercept")) {
                modelAndView.getModelMap().remove("skip_intercept");
                return;
            }
            
            userSessionService.setModelMapSessionObjects(modelAndView.getModelMap(), user);
        }
    }

    private String getCookieValue(String name, Cookie cookies[]) {

        if (cookies == null)
            return null;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }

        return null;
    }
    
    private void setCookieValue(Cookie cookies[], String name, String value) {

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                cookie.setValue(value);
            }
        }

    }
}
