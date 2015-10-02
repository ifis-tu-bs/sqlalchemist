/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 
/**
 * 
 * @author Philip
 */

import de.tu_bs.cs.ifis.sqlgame.xmlparse.Relation;
import junit.framework.*;

/**
 * Class RelationTest
 *
 * Testclass for the class Relation
 *
 * @author Philip
 */
public class RelationTest extends TestCase {
    
    /**
     * This method tests, if the intension can be set as intended.
     */
    public void testIntension() {
        Relation myRelation = new Relation();
        
        myRelation.setIntension("testintension");
        Assert.assertTrue(myRelation.getIntension().equals("testintension"));
    }
    
    /**
     * This method tests, if the tuples can be set as an Array
     */
    public void testTupleAsArray() {
        Relation myRelation = new Relation();
        
        myRelation.setTuple("testtuple");
        myRelation.setTuple("testtuple 2");
        myRelation.setTuple("testtuple 3");
        
        String[] temp = new String[3];
        temp[0] = "testtuple";
        temp[1] = "testtuple 2";
        temp[2] = "testtuple 3";
        
        for(int i = 0; i < temp.length; i++) {
            Assert.assertEquals(myRelation.getTuple().get(i), temp[i]);
        }
    }
    
    /**
     * This method tests, if the tuples can be set as a String
     */
    public void testTupleAsString() {
        Relation myRelation = new Relation();
        
        myRelation.setTuple("testtuple");
        myRelation.setTuple("testtuple 2");
        myRelation.setTuple("testtuple 3");
        
        String[] temp = new String[3];
        temp[0] = "testtuple";
        temp[1] = "testtuple 2";
        temp[2] = "testtuple 3";
        
        String s = "";
        for (String temp1 : temp) {
            s += temp1 + "\n";
        }
        
        Assert.assertTrue(myRelation.getTupleAsString().equals(s));
    }
    
}
