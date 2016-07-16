package ru.katarsis.lyra.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiPredicate;

import org.springframework.stereotype.Service;

import ru.katarsis.lyra.dto.CSVData;
import ru.katarsis.lyra.dto.DecisionTree;

@Service
public class DecisionTreeService {
	
    class SplitedIems{
        String[][] matchedItems;
        String[][] noMatchedItems;
        String predicate;
        String value;
        String attribute;
        double gain;
    }
    
    double entropyTreshold =0.01;
    
    public DecisionTreeService(){
        
    }
    
	public DecisionTreeService(CSVData rawData){
		/*this.categoryAttribute = rawData.categoryAttr;
		this.ignoredAttribute = rawData.ignoredAttr;
		String []splitedByRow = rawData.data.split("\r\n");
		trainigSet = new String [splitedByRow.length][];
		header = splitedByRow[0].split(",");
		for(int i=1;i<splitedByRow.length;i++){
		    trainigSet[i] = splitedByRow[i].split(",");
		}*/
	}
	
	public DecisionTree buildTree(String[][] items,String categoryAttr,String []header,String ignoredAttr){
	    DecisionTree tree = new DecisionTree();
	    double initialEntropy = entropy(items, getIndexOfAttib(categoryAttr, header));
	    if(initialEntropy<=entropyTreshold){
	        tree.category = getMostFrequentValue(items, getIndexOfAttib(categoryAttr, header));
	        return tree;
	    }
	    if(items.length <= 1){
	        tree.category = getMostFrequentValue(items, getIndexOfAttib(categoryAttr, header));
	        return tree;
	    }
	    SplitedIems bestSplit = new SplitedIems();
	    for(int example = items.length-1;example>0;example--){
	        Set<String> checkedPredicate = new HashSet<String>();
    	    for(int i=0;i<header.length;i++){
    	        if(header[i].equals(categoryAttr)||ignoredAttr.contains(header[i])){
    	            continue;
    	        }
    	        String condidtion ="";
    	        BiPredicate<String, String> conditionPredicate;
    	        if(isNumeric(items[example][i])){
    	            condidtion = ">=";
    	            conditionPredicate = (a,b)->Double.parseDouble(a)>=Double.parseDouble(b);
    	        }else{
    	            condidtion = "==";
    	            conditionPredicate = (a,b)->a.equals(b);
    	        }
    	        String attrPredValue = header[i]+condidtion+items[example][i];
    	        if(checkedPredicate.contains(attrPredValue)){
    	            continue;
    	        }
    	        checkedPredicate.add(attrPredValue);
    	        SplitedIems split = split(items, i, conditionPredicate, items[example][i]);
    	        double matchEntropy = entropy(split.matchedItems, getIndexOfAttib(categoryAttr, header));
    	        double noMatchEntropy = entropy(split.noMatchedItems, getIndexOfAttib(categoryAttr, header));
    	        double newEntropy = 0;
    	        newEntropy+=matchEntropy*split.matchedItems.length;
    	        newEntropy+=noMatchEntropy*split.noMatchedItems.length;
    	        newEntropy/=items.length;
    	        double currentGain = initialEntropy-newEntropy;
    	        if(currentGain>bestSplit.gain){
    	            bestSplit.attribute = header[i];
    	            bestSplit.predicate = condidtion;
    	            bestSplit.value = items[example][i];
    	            bestSplit.matchedItems =split.matchedItems;
    	            bestSplit.noMatchedItems = split.noMatchedItems;
    	            bestSplit.gain = currentGain;
    	        }
    	    }
	    }
	    
	    DecisionTree matchSubTree = buildTree(bestSplit.matchedItems, categoryAttr, header, ignoredAttr);
	    DecisionTree noMatchSubTree = buildTree(bestSplit.noMatchedItems, categoryAttr, header, ignoredAttr);
	    tree.match = matchSubTree;
	    tree.noMatch = noMatchSubTree;
	    tree.predicate =bestSplit.predicate;
	    tree.treeAttr = bestSplit.attribute;
	    tree.value = bestSplit.value;
	    return tree; 
	}
	
	private SplitedIems split(String [][]items, int attrib, BiPredicate<String,String> predicate, String value){
	    SplitedIems result = new SplitedIems();
	    List<String[]> matchedItems = new ArrayList<String[]>();
	    List<String[]> noMatchedItems = new ArrayList<String[]>();
	    for(int example =0; example<items.length;example++){
	        if(predicate.test(items[example][attrib],value)){
	            matchedItems.add(items[example]);
	        }else{
	            noMatchedItems.add(items[example]);
	        }
	    }
	    result.matchedItems = convertTo2DArray(matchedItems);
	    result.noMatchedItems = convertTo2DArray(noMatchedItems);
	    return result;
	}
	
	private String[][] convertTo2DArray(List<String[]> listItems){
	    String[][] array = new String[listItems.size()][];
	    for (int i = 0; i < listItems.size(); i++) {
	        array[i] = listItems.get(i);
	    }
	    return array;
	}
	
	private String getMostFrequentValue (String [][]items, int attrIndex){
	    HashMap<String, Integer> counter = calculateUniqeValue(items, attrIndex);
	    int mostFrequentCount = 0;
	    String mostFrequentValue = "";
	    for(Entry<String, Integer> entry : counter.entrySet()){
	        if(entry.getValue()>mostFrequentCount){
	            mostFrequentCount = entry.getValue();
	            mostFrequentValue = entry.getKey();
	        }
	            
	    }
	    return mostFrequentValue;
	}
	
	private double entropy(String [][]items, int attrib){
	    double result = 0;
	    HashMap<String, Integer> attribDistibution = calculateUniqeValue(items, attrib);
	    for(Entry<String,Integer> item: attribDistibution.entrySet()){
	        double avrCount = (double)item.getValue()/items.length;
	        result += -1*(avrCount)*Math.log(avrCount);
	    }
	    return result;
	}
	
	private HashMap<String, Integer> calculateUniqeValue(String [][]items, int attrIndex){
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
	        if(header[i].equals(attribName)){
	            result = i ;
	            break;
	        }
	    }
	    return result; 
	}
	
	public static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  
	}
}
