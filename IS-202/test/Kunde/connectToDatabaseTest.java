/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Kunde;

import Database.connectToDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
 * @author Erlend Thorsen
 */
public class connectToDatabaseTest {
    
    public connectToDatabaseTest() {
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
     * Test of init method, of class connectToDatabase.
     */
    @Test
    public void testInit() {
        System.out.println("init");
        connectToDatabase instance = new connectToDatabase();
        instance.init();
        Connection con = instance.getConnection();
        ResultSet rs = null;
        PreparedStatement test;
        try {
            System.out.println("working 1!");
            test = con.prepareStatement("SELECT * from kunde LIMIT 1");
            System.out.println("working 2!");
            rs = test.executeQuery();
            System.out.println("working 3!");
            while(rs.next()){
                String s = rs.getString(2);
                System.out.println(s);
            }
            System.out.println("working 4!");
            
        } catch (SQLException ex) {
            Logger.getLogger(connectToDatabaseTest.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("sql execute failed!");
        }
        rs = null;
        instance.close(rs);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getConnection method, of class connectToDatabase.
     */
    /*@Test
    public void testGetConnection() {
        System.out.println("getConnection");
        connectToDatabase instance = new connectToDatabase();
        Connection expResult = null;
        Connection result = instance.getConnection();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of close method, of class connectToDatabase.
     */
    /*@Test
    public void testClose_ResultSet() {
        System.out.println("close");
        ResultSet rs = null;
        connectToDatabase instance = new connectToDatabase();
        instance.close(rs);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of close method, of class connectToDatabase.
     */
    /*@Test
    public void testClose_Statement() {
        System.out.println("close");
        Statement stmt = null;
        connectToDatabase instance = new connectToDatabase();
        instance.close(stmt);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of destroy method, of class connectToDatabase.
     */
    /*@Test
    public void testDestroy() {
        System.out.println("destroy");
        connectToDatabase instance = new connectToDatabase();
        instance.destroy();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
    
}
