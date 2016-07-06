/**
 * @author Petr Klimov
 * 
 */
package ru.katarsis.lyra.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DefaultDashboard {

    @RequestMapping(value="/dashboard/default", method = RequestMethod.GET)
    public String showDefaultDashboard(){
        return "/dashboard/default";
    }
}
