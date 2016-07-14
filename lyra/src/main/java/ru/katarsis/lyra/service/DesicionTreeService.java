package ru.katarsis.lyra.service;

import java.util.HashMap;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import ru.katarsis.lyra.dto.CSVData;
import ru.katarsis.lyra.dto.DesicionTree;

@Service
public class DesicionTreeService {
	
    /*String header[];
    String[][] trainigSet;
    String categoryAttribute;
    String ignoredAttribute;
	*/
	public DesicionTreeService(CSVData rawData){
		/*this.categoryAttribute = rawData.categoryAttr;
		this.ignoredAttribute = rawData.ignoredAttr;
		String []splitedByRow = rawData.data.split("\r\n");
		trainigSet = new String [splitedByRow.length][];
		header = splitedByRow[0].split(",");
		for(int i=1;i<splitedByRow.length;i++){
		    trainigSet[i] = splitedByRow[i].split(",");
		}*/
	}
	
	public DesicionTree buildTree(String[][] items,String categoryAttr,String []header,String ignoredAttr){
	    DesicionTree tree = new DesicionTree();
	    double initialEntropy = entropy(items, getIndexOfAttib(categoryAttr, header));
	    for(int i=0;i<header.length;i++){
	        
	    }
	    return null; 
	}
	
	private double entropy(String [][]items, int attrib){
	    double result = 0;
	    HashMap<String, Integer> attribDistibution = calculateUniqeValue(items, attrib);
	    for(Entry<String,Integer> item: attribDistibution.entrySet()){
	        int avrCount = item.getValue()/items.length;
	        result += -1*(avrCount)*Math.log(avrCount);
	    }
	    return result;
	}
	
	private HashMap<String, Integer> calculateUniqeValue(String [][]items, int attrIndex){
	    int[] result;
	    HashMap<String, Integer> uniqueValues = new HashMap<>(); 
	    for(int i=0;i<items.length;i++){
	        uniqueValues.put(items[i][attrIndex], 0);
	    }
	    for(int i=0; i<items.length;i++){
	        String key = items[i][attrIndex];
	        int prev = uniqueValues.get(key)+1;
	        uniqueValues.put(key, prev);
	    }
	    
	    return uniqueValues;
	}
	
	private int getIndexOfAttib(String attribName,String []header){
	    int result =-1;
	    for(int i=0; i<header.length;i++){
	        if(header.equals(attribName)){
	            result = i ;
	            break;
	        }
	    }
	    return result; 
	}
}
