/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import de.tu_bs.cs.ifis.sqlgame.xmlparse.Header;
import junit.framework.*;
 /*
 * @author Philip
 */

/**
 * Class HeaderTest
 *
 * Testclass for the class Header
 *
 * @author Philip
 */
public class HeaderTest extends TestCase {

    /**
     * This method tests, if the title can be set as intended.
     */
    public void testTitle() {
        Header myHeader = new Header();
        
        myHeader.setTitle("testtitle");
        myHeader.setTitle("testtitle 2");
        myHeader.setTitle("testtitle 3");
        
        String[] temp = new String[3];
        temp[0] = "testtitle";
        temp[1] = "testtitle 2";
        temp[2] = "testtitle 3";
        
        for(int i = 0; i < temp.length; i++) {
            Assert.assertEquals(myHeader.getTitle().get(i), temp[i]);
        }
    }
    
    /**
     * This method tests, if the flufftext can be set as intended.
     */
    public void testFlufftext() {
        Header myHeader = new Header();
        
        myHeader.setFlufftext("testtext");
        myHeader.setFlufftext("testtext 2");
        myHeader.setFlufftext("testtext 3");
        
        String[] temp = new String[3];
        temp[0] = "testtext";
        temp[1] = "testtext 2";
        temp[2] = "testtext 3";
        
        for(int i = 0; i < temp.length; i++) {
            Assert.assertEquals(myHeader.getFlufftext().get(i), temp[i]);
        }
    }
    
    /**
     * This method tests, if the language can be set as intended.
     */
    public void testLanguage() {
        Header myHeader = new Header();
        
        myHeader.setLanguage("testlang");
        Assert.assertTrue(myHeader.getLanguage().equals("testlang"));
    }
    
    /**
     * This method tests, if the taskID can be set as intended.
     */
    public void testTaskId() {
        Header myHeader = new Header();
        
        myHeader.setTaskId("testid");
        Assert.assertTrue(myHeader.getTaskId().equals("testid"));
    }
}
   

