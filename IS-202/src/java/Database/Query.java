/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Servlets.Login;
import java.io.InputStream;
import java.sql.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vegar
 */
public class Query {
    private connectToDatabase ctd;
    private Connection con;
    private ResultSet rs;
    private PreparedStatement statement;
            
    //Setter opp objektet med connection klar for statemtents
    public Query(){    
        ctd = new connectToDatabase();
        ctd.init();
        con = ctd.getConnection();
    }
    
    //For insert, update og delete etc.
    public void update(String Query) {
        try {
            statement = con.prepareStatement(Query);
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    //for select. returnerer resultset
    public ResultSet query(String Query) {
        try {
            statement = con.prepareStatement(Query);
            rs = statement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    //Legger en fil inn i ett statement gjennom inputstream og legger inn i database
    public void insertFile(String query, InputStream is, String name) {
        try {
            statement = con.prepareStatement(query);
            statement.setString(1, name);
            statement.setBlob(2,is);
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void close() {
        try {
            if (rs != null) {
                rs.close();
            }
            if  (statement!=null)  {
                statement.close();
            }
            con.close();
            ctd.destroy();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
