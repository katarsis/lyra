/**
 * @author Petr Klimov
 * 
 */
package ru.katarsis.lyra.controllers;

import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.katarsis.lyra.dto.CSVData;
import ru.katarsis.lyra.dto.DecisionTree;
import ru.katarsis.lyra.service.DecisionTreeService;

@Controller
public class DecisionTreeController {

	@Autowired
	DecisionTreeService decisionTreeService;
	
    @RequestMapping(value="/dashboard/decision/tree", method = RequestMethod.GET)
    public String showDefaultDashboard(Locale locale, Model model){
    	model.addAttribute("csvData",new CSVData());
        return "/dashboard/decision_tree";
    }
    
    @RequestMapping(value="/dashboard/decision/tree/makeModel", method = RequestMethod.POST)
    @ResponseBody
    public DecisionTree makeModel(@RequestBody CSVData data,HttpServletResponse response){
    	String []splitedByRow = data.data.split("\n");
    	String []header = splitedByRow[0].split(",");
    	String[][] trainigSet = new String [splitedByRow.length-1][];
		header = splitedByRow[0].split(",");
		for(int i=1;i<splitedByRow.length;i++){
		    trainigSet[i-1] = splitedByRow[i].split(",");
		}
    	DecisionTree tree = decisionTreeService.buildTree(trainigSet, data.getCategoryAttr(), header, data.getIgnoredAttr());
     	response.setHeader("Content-Disposition","inline");
    	return tree;
    }
   
}
