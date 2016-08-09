/**
 * @author Petr Klimov
 * 
 */
package ru.katarsis.lyra.controllers;

import java.io.File;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import ru.katarsis.lyra.service.UserFilesService;
import ru.katarsis.lyra.service.UserSessionService;

@Controller
public class DecisionTreeController {
    
    private static final Logger logger = LoggerFactory.getLogger(DecisionTreeController.class);
	
    @Autowired
	DecisionTreeService decisionTreeService;
    
    @Autowired 
    UserSessionService userSessionService;
    
    @Autowired
    UserFilesService userFilesService;
	
    @RequestMapping(value="/dashboard/decision/tree", method = RequestMethod.GET)
    public String showDefaultDashboard(Locale locale, Model model){
    	model.addAttribute("csvData",new CSVData());
        return "/dashboard/decision_tree";
    }
    
    @RequestMapping(value="/dashboard/decision/tree/makeModel", method = RequestMethod.POST)
    @ResponseBody
    public DecisionTree makeModel(@RequestBody CSVData data,HttpServletResponse response){
        logger.info("Get make decision tree model request");
        DecisionTree tree = null;
        try{
            String []splitedByRow = null;
            if(data.fileName!=null&&!data.fileName.isEmpty()){
                splitedByRow = userFilesService.getDataFromFileSource(new File( "D:\\lyra\\upload_dir"+File.separator+userSessionService.getCurrentUserName()+File.separator+data.fileName)).replace("\r", "").split("\n");
            }else{
                splitedByRow = data.data.split("\n");
            }
        	String []header = splitedByRow[0].split(",");
        	String[][] trainigSet = new String [splitedByRow.length-1][];
    		header = splitedByRow[0].split(",");
    		for(int i=1;i<splitedByRow.length;i++){
    		    trainigSet[i-1] = splitedByRow[i].split(",");
    		}
        	tree = decisionTreeService.buildTree(trainigSet, data.getCategoryAttr(), header, data.getIgnoredAttr());
         	response.setHeader("Content-Disposition","inline");
            logger.info("Make decision tree model request successfully finished");
        }catch(Exception err){
            logger.error("Error while trying create decision tree model: ",err);
        }
    	return tree;
    }
    
    
   
    
}
