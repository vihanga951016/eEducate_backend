package com.vihanga.eEducate.listener;

import com.vihanga.eEducate.service.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class AuthenticationFaliureListener {

    private LoginAttemptService loginAttemptService;

    @Autowired
    public AuthenticationFaliureListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    /*
    * If user is failed to login, that act as an event and below event listener method will set the user's username
    * who is the generator of the login failure, to the addUserToLoginAttemptCache() method in LoginAttemptService
    * class.
    */
    @EventListener
    public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) throws ExecutionException {
        Object principle = event.getAuthentication().getPrincipal();
        if(principle instanceof String){
            String username = (String) event.getAuthentication().getPrincipal();
            loginAttemptService.addUserToLoginAttemptCache(username);
        }
    }
}
