/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Servlets.Login;
import java.io.PrintWriter;
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
    
    public Query(){
        ctd = new connectToDatabase();
        ctd.init();
        con = ctd.getConnection();
    }
    
    //For insert, update og delete etc.
    public void update(String Query) {
        try {
            PreparedStatement update = con.prepareStatement(Query);
            update.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    //for select. returnerer resultset
    public ResultSet query(String Query) {
        try {
            PreparedStatement query = con.prepareStatement(Query);
            rs = query.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    
    /*@param selectNoe er MySQL-teksten som skal sendes som query (prepareStatement)
    @param selected er table-navn
    @param out er for at PrintWriter skal skrive ut.*/
    public void skrivModulliste(String selectNoe, String selected, PrintWriter out) {
        try {
            PreparedStatement query = con.prepareStatement(selectNoe);
            rs = query.executeQuery();
            out.println("<u1>");
            //Skriver ut felt en og to for hver rad i query + setter et felt lik id til bruker som sendes videre hvis noen skal endre informasjonen om en bruker
            while(rs.next()){
                out.println("<th>" + rs.getString(2) +" "+ rs.getString(3) + "</th>" + "<td><a href ='LeggTilModul?id="+"rs.getString(4)"+"'></td>");
            }
            /*Itererer gjennom lista og returnerer det som står på kolonne 1, 2 og 3, i den rekkefølgen.*/ 
            out.println("</u1>");
            rs = null;
            
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}