/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Kunde;

import Database.registrerKunde;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Erlend Thorsen
 */
public class registrerKundeTest {
    
    public registrerKundeTest() {
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
     * Test of alleredeKunde method, of class registrerKunde.
     */
    @Test
    public void testAlleredeKunde() {
        System.out.println("alleredeKunde");
        String epost = "ola@nordmann.no";
        registrerKunde instance = new registrerKunde();
        boolean expResult = false;
        boolean result = instance.alleredeKunde(epost);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
