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
        
        public void printLeftSidebar(String active, String kursId, PrintWriter out){
            String hjem = "";
            String personer = "";
            String moduler = "";
            String grupper = "";
            String kunngjøringer = "";
            String activeStyle = "style='color:orange;'";
            if(active == "Hjem"){
                hjem = activeStyle;
            }
            else if(active == "Personer"){
                personer = activeStyle;
            }
            else if(active == "Moduler"){
                moduler = activeStyle;
            }
            else if(active == "Grupper"){
                grupper = activeStyle;
            }           
            else if(active == "Kunngjøringer"){
                kunngjøringer = activeStyle;
            }  
            out.println("<div class='leftSidebar'>");
            out.println("<ul>");
            out.println("<li "+hjem+"><a href='Kurs?kursId="+kursId+"'>Hjem</a></li>");
            out.println("<li "+kunngjøringer+"><a href='Kunngjoringer?kursId="+kursId+"'>Kunngjøringer</a></li>");
            out.println("<li "+personer+" ><a href=\"BrukerListeKurs?kursId="+kursId+"&redigerBrukere=false\">Personer</a></li>");
            out.println("<li "+moduler+"><a href='ModulListe?kursId="+kursId+"'>Moduler</a></li>");
            out.println("<li "+grupper+"><a href='GruppeListe?kursId="+kursId+"'>Grupper</a></li>");

            out.println("</ul>");
            out.println("</div>");
        }
        
        private void print(String type, String active, String id, boolean isForeleser, JspWriter JW, PrintWriter PW) throws IOException, SQLException{

            
            String classForside = "";
            String classLister = "";
            String classKurs = "";
            String classInnstillinger = "";
            String classKalender = "";
            //Sjekker Hvilken link i navbar som skal være "active"
            if(active == "Forside"){
                classForside = "class='active'";
            }
            else if(active == "Lister"){
                classLister = "class='active'";
            }
            else if(active == "Kurs"){
                classKurs = "style='color:orange'";
            }
            else if(active == "Kalender"){
                classKalender = "class='active'";
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
            navArray.add("<a href='GruppeListe'>Gruppeliste</a>");
            navArray.add("<a href='NotifikasjonsListe'>NotifikasjonsListe</a>");
            navArray.add("</div>");
            navArray.add("<li class='dropdown'>");
            navArray.add("<a "+classKurs+" href='javascript:void(0)' class='dropbtn'>Kurs</a>");
            navArray.add("<div class='dropdown-content'>");
            
            try{
                //Databasetilkobling
                Query query = new Query();
                ResultSet rs = null;
                ResultSet rs2 = null;
                
                //velger tabell baser på om personen er student eller foreleser 
                if(isForeleser){
                    rs = query.query("Select * from foreleserKurs where foreleserId ='"+id+"'");
                }else{
                    rs = query.query("Select * from tarKurs where studentId ='"+id+"'");
                }
                //Legger til kurs personen er medlem i navbar
                while(rs.next()){                  
                    String kursId = rs.getString(1);
                    navArray.add("<a href='Kurs?kursId="+kursId+"'>"+kursId+"</a>");
                }
                query.close();
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Om person er foreleser vil man få opp muligheten til å legge til kurs
            if(isForeleser){
                navArray.add("<a href='KursListe'>Legg til kurs</a>");
            }
            navArray.add("</div>");
            navArray.add("<li class='dropdown'>");
            navArray.add("<a "+classKalender+" href='Kalender'>Kalender</a>");
            navArray.add("<div class='dropdown-content'>");
            navArray.add("<li style='float:right'><a href='LoggUt'>Logg ut</a><li>");
            navArray.add("<li style='float:right'><a "+classInnstillinger+" href='Innstillinger?edit=false'>Innstillinger</a></li>");
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
