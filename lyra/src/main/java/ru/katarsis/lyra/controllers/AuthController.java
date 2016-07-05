/**
 * @author Petr Klimov
 * 
 */
package ru.katarsis.lyra.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthController {

    @RequestMapping(value ="/login", method = RequestMethod.GET)
    public String doLogin(){
        return "/auth/login";
    }
}
