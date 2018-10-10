/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Database.Query;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.jsp.JspWriter;

/**
 *
 * @author Erlend Thorsen
 */
public class Navbar {
    
        public Navbar(){
            
        }
        
    /**
     *
     * @param active
     * @param out
     * @throws java.io.IOException
     */
        public void printNavbar(String active, String id, boolean isForeleser, PrintWriter out) throws IOException, SQLException{
            
            print("PW", active, id, isForeleser, null, out);
            
        }
    
        public void printNavbarJSP(String active, String id, boolean isForeleser, JspWriter out) throws IOException, SQLException{
            
            print("JSP", active, id, isForeleser, out, null);

        }
        
        private void print(String type, String active, String id, boolean isForeleser, JspWriter JW, PrintWriter PW) throws IOException, SQLException{

            
            String classForside = "";
            String classLister = "";
            String classKurs = "";
            String classInnstillinger = "";
            //Sjekker Hvilken link i navbar som skal være "active"
            if(active == "Forside"){
                classForside = "class='active'";
            }
            else if(active == "Lister"){
                classLister = "class='active'";
            }
            else if(active == "Kurs"){
                classKurs = "class='active'";
            }
            else if(active == "Innstillinger"){
                classInnstillinger = "class='active'";
            }
                                
            //navbar i array
            ArrayList<String> navArray = new ArrayList<String>();
            navArray.add("<div class='topnav'>");
            navArray.add("<ul>");
            navArray.add("<li><a "+classForside+" href='forside.jsp'>Forside</a></li>");
            navArray.add("<li class='dropdown'>");
            navArray.add("<a href='javascript:void(0)' class='dropbtn'>Lister</a>");
            navArray.add("<div class='dropdown-content'>");
            navArray.add("<a href='BrukerListe'>Brukerliste</a>");
            navArray.add("<a href='ModulListe'>Modulliste</a>");
            navArray.add("</div>");
            navArray.add("<li class='dropdown'>");
            navArray.add("<a href='javascript:void(0)' class='dropbtn'>Kurs</a>");
            navArray.add("<div class='dropdown-content'>");
            
            try{
                //Databasetilkobling
                Query query = new Query();
                ResultSet rs = null;
                ResultSet rs2 = null;
                if(isForeleser){
                    rs = query.query("Select * from foreleserKurs where foreleserId ='"+id+"'");

                }else{
                    rs = query.query("Select * from tarKurs where studentId ='"+id+"'");
                }
                
                while(rs.next()){                  
                    String kursId = rs.getString(1);
                    rs2 = query.query("Select kursId,kursNavn from kurs where id ='"+kursId+"'");
                    rs2.next();
                    String kursKode = rs2.getString(1);
                    String kursNavn = rs2.getString(2);
                    navArray.add("<a href='Kurs?kursId="+kursId+"&kursKode="+kursKode+"&kursNavn="+kursNavn+"'>"+kursKode+"</a>");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(isForeleser){
                navArray.add("<a href='LeggTilKurs'>Legg til kurs</a>");
            }
            navArray.add("</div>");
            navArray.add("<li style='float:right'><a href='LoggUt'>Logg ut</a><li>");
            navArray.add("<li "+classInnstillinger+" style='float:right'><a href='instillinger'>Innstillinger</a></li>");
            navArray.add("</ul>");
            navArray.add("</div>");
            
            //velger type Writer og skriver ut navbar
            if(type == "JSP"){
                
                for(String linje: navArray) {
                    JW.println(linje);
                }
                
            }else{
              
                for(String linje: navArray) {
                    PW.println(linje);
                }
                
            }
        }
}
