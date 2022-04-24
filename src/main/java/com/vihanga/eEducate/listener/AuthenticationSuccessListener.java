package com.vihanga.eEducate.listener;

import com.vihanga.eEducate.domain.HigherStaff;
import com.vihanga.eEducate.domain.HigherStaffPrincipal;
import com.vihanga.eEducate.service.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener {

    private LoginAttemptService loginAttemptService;

    @Autowired
    public AuthenticationSuccessListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @EventListener
    public void onAuthenticatonSuccess(AuthenticationSuccessEvent event){
        Object principle = event.getAuthentication().getPrincipal();
        if(principle instanceof HigherStaffPrincipal){
            HigherStaffPrincipal higherStaff = (HigherStaffPrincipal) event.getAuthentication().getPrincipal();
            loginAttemptService.evictUserFromLoginAttemptCache(higherStaff.getUsername());
        }
    }
}
