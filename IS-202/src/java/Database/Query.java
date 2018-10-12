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
    
    public void close() {
        try {
            if (rs != null) {
                rs.close();
            }
            statement.close();
            con.close();
            ctd.destroy();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*@param selectNoe er MySQL-teksten som skal sendes som query (prepareStatement)
    @param selected er table-navn
    @param out er for at PrintWriter skal skrive ut.*/
    public void skrivModulliste(String selectNoe, PrintWriter out) {
        try {
            
            //Skriver ut felt en og to for hver rad i query + setter et felt lik id til bruker som sendes videre hvis noen skal endre informasjonen om en bruker
            out.println("<tr>");
            out.println("<th></th>");
            int antallModuler = 0;
            rs = query("SELECT modul_Navn, modul_Nummer from ModulListe");
            while(rs.next()){
                    //out.println("<a href ='LeggTilModul?id="+rs.getString(1)+rs.getString(2)+"'>");
                    out.println("<th>" + rs.getString(1) +" "+ rs.getString(2) + "</th></a>");
                    antallModuler++;
            }
            //PreparedStatement query2 = con.prepareStatement(selectNoe);
            rs = query(selectNoe);
            out.println("</tr>");
            out.println("<tr>");
            rs.beforeFirst();
            boolean a = false;
            while (rs.next()){  //Itererer gjennom tablet i databasen
                out.println("<tr>");
                out.println("<td class='student'>" + rs.getString(1) +" "+ rs.getString(2) + " </td>");
                int studentId = rs.getInt(5);
                boolean rsLast = false;
                int tellerI = 0;
                while (tellerI<antallModuler)   {    
                    tellerI++;
                    if (rs.getInt(5) == studentId && rs.isLast()!=true) {
                        if (rs.getInt(6)==tellerI) {    //Feilen er her. ModulNummer fra MYsql vs teller ant moduler =/ samme etter sletting av moduler
                            out.println("<td> Poengsum " + rs.getString(4) +" </td>");
                            rs.next();
                        } else {
                            out.println("<td>  </td>");
                        }
                        
                            //if (rs.getInt(6)!=tellerI)  {
                                
                            //}
                    } else if (rs.getInt(5) == studentId && rs.isLast()==true && rsLast==false){
                        out.println("<td> Poengsum " + rs.getString(4) +" </td>");
                        rsLast = true;
                    }
                    else    {
                       out.println("<td>  </td>");
                    }
                }
                if (rs.isLast()!=true) {
                    rs.previous();
                } else if (rs.isLast()==true && a == false) {
                    a =true;
                    rs.previous();
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
