/**
 * @author Petr Klimov
 * 
 */
package ru.katarsis.lyra.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@Test
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml" })
public class TestKmeansService extends AbstractTestNGSpringContextTests {

    @Autowired
    KMeanService kMeanService;
    
    private Map<String,Double> classificationItem;
    
    private Map<Map<String,Double>,String> traningDataSet;
    
    @BeforeTest
    void setupData(){
        classificationItem = new HashMap<String, Double>();
        classificationItem.put("x", 0.0);
        classificationItem.put("y", 0.0);
        
        traningDataSet = new HashMap<>();
        traningDataSet.put(new HashMap<String, Double>(){{put("x", 0.1);put("y", 0.1);}}, "A");
        traningDataSet.put(new HashMap<String, Double>(){{put("x", 0.1);put("y", 0.0);}}, "A");
        traningDataSet.put(new HashMap<String, Double>(){{put("x", 1.1);put("y", 1.1);}}, "B");
        traningDataSet.put(new HashMap<String, Double>(){{put("x", 1.0);put("y", 1.1);}}, "B");
        
    }
    
    @Test
    void testKmeanClassification(){
        String result = kMeanService.doClassification(classificationItem, traningDataSet, 3);
        Assert.assertNotNull(result);
        Assert.assertEquals("B", result);
    }
}
