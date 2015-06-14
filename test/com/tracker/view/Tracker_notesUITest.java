
package com.tracker.view;

import java.awt.Component;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Tahmina Khan
 */
public class Tracker_notesUITest {
    
    public Tracker_notesUITest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getComponentByName method, of class Tracker_notesUI.
     */
    @Test
    public void testGetComponentByName() {
        System.out.println("getComponentByName");
        String name = "";
        Tracker_notesUI instance = null;
        try {
            instance = new Tracker_notesUI();
        } catch (IOException ex) {
            Logger.getLogger(Tracker_notesUITest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Tracker_notesUITest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Component expResult = null;
        Component result = instance.getComponentByName(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class Tracker_notesUI.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        Tracker_notesUI.main(args);
        // TODO review the generated test code and remove the default call to fail.
       // fail("The test case is a prototype.");
    }
    
}
