/**
 * @author Petr Klimov
 * 
 */
package ru.katarsis.lyra.controllers;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ErrorController {

    @RequestMapping(value = "404.html", method = RequestMethod.GET)
    public String show404(Locale locale, Model model){
        return "/404";
    }
}
