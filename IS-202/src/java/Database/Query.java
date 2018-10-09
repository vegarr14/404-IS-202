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
            
    //Setter opp objektet med connection klar for statemtents
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
            
            //Skriver ut felt en og to for hver rad i query + setter et felt lik id til bruker som sendes videre hvis noen skal endre informasjonen om en bruker
            out.println("<tr>");
            out.println("<th></th>");
            while(rs.next()){
                    //out.println("<a href ='LeggTilModul?id="+rs.getString(1)+rs.getString(2)+"'>");
                    out.println("<th>" + rs.getString(1) +" "+ rs.getString(2) + "</th></a>");
            }
            out.println("</tr>");
            out.println("<tr>");
            rs.beforeFirst();
            while (rs.next()){  //Itererer gjennom tablet i databasen
                out.println("<tr>");
                out.println("<td class='student'>" + rs.getString(3) + " " + rs.getString (4) + " </td>");
                int tellerI = 0;
                int max_modul_Nummer = 5;
                while (tellerI<max_modul_Nummer)   {
                        tellerI++;
                        out.println("<td class='kommentar'> Kommentarer nr " + tellerI +" </td>");
                    }
                out.println("<td id='prosent'> p%" + "33%" + "</td>");
                out.println("</tr>");
            }
            out.println("</tr>");
            rs.beforeFirst();
            
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
