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
                //<a href ='LeggTilModul?id="+"rs.getString(4)"+"'>
                    out.println("<th>" + rs.getString(2) +" "+ rs.getString(3) + "</th>");
            }
            out.println("</tr>");
            out.println("<tr>");
            rs.beforeFirst();
            while (rs.next()){
                out.println("<tr>");
                out.println("<td class='student'> Student nr " + rs.getString(3) + " </td>");
                int tellerI = 0;
                while (tellerI<5)   {
                        tellerI++;
                        out.println("<td class='kommentar'> Kommentarer nr " + tellerI +" </td>");
                    }
                out.println("<tp class='prosent'> Prosentvis framgang? " + "33%" + "</tp>");
                out.println("</tr>");
            }
            out.println("</tr>");
            
            /*out.println("</tr>");
            rs.beforeFirst();
            out.println("<tr>");
            while (rs.next()){
                    out.println("<td class='student'> Student nr " + rs.getString(3) + " </td>");
            }
            out.println("</tr>");
            rs.beforeFirst();
            out.println("<tr>");
            while (rs.next()){
                    out.println("<td class='kommentar'> Kommentarer nr " + rs.getString(3) +" </td>");
            }
            out.println("</tr>");
            rs.beforeFirst();
            out.println("<tr>");
            while (rs.next()){
                    out.println("<td class='prosent'> Prosentvis framgang? " + "33%" + "</td>");
            }
            out.println("</tr>");
            rs.beforeFirst();
                    //out.println("</a>");
            
            /*Itererer gjennom lista og returnerer det som står på kolonne 1, 2 og 3, i den rekkefølgen.*/ 
            //query.close();*/
            
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
