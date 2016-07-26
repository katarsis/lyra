/**
 * @author Petr Klimov
 * 
 */
package ru.katarsis.lyra.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ru.katarsis.lyra.model.Role;
import ru.katarsis.lyra.model.UserAccount;
import ru.katarsis.lyra.repository.RoleRepository;
import ru.katarsis.lyra.repository.UserAccountRepository;

@Controller
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	@Autowired
	UserAccountRepository userAccountRepository;
	
	@Autowired
	RoleRepository roleRepository;

    @RequestMapping(value ="/login", method = RequestMethod.GET)
    public String doLogin(){
        return "/auth/login";
    }
    
    @RequestMapping(value ="/registration", method= RequestMethod.GET)
    public String doRegistration(Locale locale, Model model){
    	model.addAttribute("user", new UserAccount());
    	return "/auth/registration";
    }
    
    @RequestMapping(value = "/doregistration", method = RequestMethod.POST)
	public String doRegistration(@ModelAttribute ("user") UserAccount user, BindingResult result){
        logger.info("Get registration request ");
        try{
    		List<Role> userDefaultRole = new ArrayList<Role>();
    		Role defaultRole = roleRepository.getRoleById(1);
    		userDefaultRole.add(defaultRole);
    		user.setRoles(userDefaultRole);
    		userAccountRepository.save(user);
    		logger.info("Registration finished successfully");
        }catch(Exception err){
            logger.error("Error while trying registration user:",err);
        }
		return "redirect:/";
	}
    
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String doLogout(Model model){
    	return "redirect:/";
    }
}
