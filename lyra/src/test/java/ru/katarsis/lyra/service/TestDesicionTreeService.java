/**
 * @author Petr Klimov
 * 
 */
package ru.katarsis.lyra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ru.katarsis.lyra.dto.DecisionTree;

@Test
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml" })
public class TestDesicionTreeService extends AbstractTestNGSpringContextTests {

    private String[][] testItems;
    private String[] header;
    
    @Autowired
    DecisionTreeService decisionTreeService;

    @BeforeClass
    private void setUpTest(){
        testItems = new String[9][];
        testItems[0] = new String[]{"Homer","0","250","36","male"};
        testItems[1] = new String[]{"Marge","10","150","34","female"};
        testItems[2] = new String[]{"Bart","2","90","10","male"};
        testItems[3] = new String[]{"Lisa","6","78","8","female"};
        testItems[4] = new String[]{"Maggie","4","20","1","female"};
        testItems[5] = new String[]{"Abe","1","170","70","male"};
        testItems[6] = new String[]{"Selma","8","160","41","female"};
        testItems[7] = new String[]{"Otto","10","180","38","male"};
        testItems[8] = new String[]{"Krusty","6","200","45","male"};
        header = new String[]{"person","hairLength","weight","age","sex"};
    }
    
    @Test()
    void testDecisionGenerator() {
        DecisionTree tree = decisionTreeService.buildTree(testItems, "sex", header, "person");
        Assert.assertEquals(tree.treeAttr+tree.predicate+tree.value, "weight>=170");
        Assert.assertEquals(tree.match.category, "male");
        Assert.assertEquals(tree.noMatch.treeAttr+tree.noMatch.predicate+tree.noMatch.value, "hairLength>=4");
        Assert.assertEquals(tree.noMatch.match.category, "female");
        Assert.assertEquals(tree.noMatch.noMatch.category, "male");
    }

}