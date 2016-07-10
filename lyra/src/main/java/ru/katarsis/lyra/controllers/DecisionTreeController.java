/**
 * @author Petr Klimov
 * 
 */
package ru.katarsis.lyra.controllers;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ru.katarsis.lyra.dto.CSVData;

@Controller
public class DecisionTreeController {

    @RequestMapping(value="/dashboard/decision/tree", method = RequestMethod.GET)
    public String showDefaultDashboard(Locale locale, Model model){
    	model.addAttribute("csvData",new CSVData());
        return "/dashboard/decision_tree";
    }
    
    @RequestMapping(value="/dashboard/decision/tree/makeModel", method = RequestMethod.POST)
    public String makeModel(@ModelAttribute ("csvData") CSVData data){
    	System.out.println(data);
    	return "/dashboard/decision_tree";
    }
}
