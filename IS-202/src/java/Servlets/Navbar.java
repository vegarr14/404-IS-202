/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
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
     */
        public void printNavbar(String active, PrintWriter out) throws IOException{
            
            print("PW", active, null, out);
            
        }
    
        public void printNavbarJSP(String active, JspWriter out) throws IOException{
            
            print("JSP", active, out, null);

        }
        
        private void print(String type, String active, JspWriter JW, PrintWriter PW) throws IOException{
            
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
            String[] navArray = new String[21];
            navArray[0] = "<div class='topnav'>";
            navArray[1] = "<ul>";
            navArray[2] = "<li><a "+classForside+" href='forside.jsp'>Forside</a></li>";
            navArray[3] = "<li class='dropdown'>";
            navArray[4] = "<a href='javascript:void(0)' class='dropbtn'>Lister</a>";
            navArray[5] = "<div class='dropdown-content'>";
            navArray[6] = "<a href='BrukerListe'>Brukerliste</a>";
            navArray[7] = "<a href='ModulListe'>Modulliste</a>";
            navArray[8] = "<a href='GruppeListe'>Gruppeliste</a>";
            navArray[9] = "</div>";
            navArray[10] = "<li class='dropdown'>";
            navArray[11] = "<a href='javascript:void(0)' class='dropbtn'>Kurs</a>";
            navArray[12] = "<div class='dropdown-content'>";
            navArray[13] = "<a href='#'>IS-200</a>";
            navArray[14] = "<a href='#'>IS-201</a>";
            navArray[15] = "<a href='#'>IS-202</a>";
            navArray[16] = "</div>";
            navArray[17] = "<li style='float:right'><a href='LoggUt'>Logg ut</a><li>";
            navArray[18] = "<li "+classInnstillinger+" style='float:right'><a href='instillinger'>Innstillinger</a></li>";
            navArray[19] = "</ul>";
            navArray[20] = "</div>";
            
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
