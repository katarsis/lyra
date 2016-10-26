/**
 * @author Petr Klimov
 * 
 */
package ru.katarsis.lyra.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class KMeanService {
    
    private class Metrics{
        public Double dist;
        public String objClass;
        
        @Override
        public String toString() {
            return "Metrics [dist=" + dist + ", objClass=" + objClass + "]";
        }
    }

    public KMeanService(){
        
    }
    
    public String doClassification(Map<String, Double> itemCharacteristic, Map<Map<String, Double>, String> dataSet, int kMeans){
        String result = null;
        List<Metrics> resultList = new ArrayList<>();
        for(Entry<Map<String, Double>,String> dataSetItem: dataSet.entrySet()){
            Double distance = getEuclidianDistance(itemCharacteristic, dataSetItem.getKey());
            Metrics item = new Metrics();
            item.dist = distance;
            item.objClass = dataSetItem.getValue();
            resultList.add(item);
        }
        int step = 0;
        Map<Object,Long> map  = resultList.stream().sorted((e1,e2)->e1.dist.compareTo(e2.dist)).map(m->m.objClass).limit(kMeans)
                                .collect(Collectors.groupingBy(o->o, Collectors.counting()));
        long max = 0;
        for(Entry<Object,Long> item: map.entrySet()){
            if(item.getValue()>max){
                result = (String)item.getKey();
            }
        }
        return result;
    }
    
    private Double getEuclidianDistance(Map<String, Double> itemFeatureVec, Map<String,Double> dataSetVec){
        Double result = null;
        Double sumSquare = 0.0;
        for(String itemFeatureKey:itemFeatureVec.keySet()){
            Double valIt = itemFeatureVec.get(itemFeatureKey);
            Double valDs = dataSetVec.get(itemFeatureKey);
            sumSquare+=Math.pow((valIt-valDs),2);
        }
        result =Math.pow(sumSquare,0.5);
        return result;
    }
}
