/**
 * @author Petr Klimov
 * 
 */
package ru.katarsis.lyra.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserSessionService {

    
    public String getCurrentUserName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsAdapter activeUser = authentication == null ? null : (UserDetailsAdapter)authentication.getPrincipal();
        return activeUser.getUsername();
    }
}
