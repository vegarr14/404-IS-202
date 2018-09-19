/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import java.io.PrintWriter;

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
    public void printNavbar(String active, PrintWriter out){
               
            out.println("<div class='topnav'>");
            out.println("<ul>");
            out.println("<li><a class='active' href='Hjem'>Hjem</a></li>");
            out.println("<li class='dropdown'>");
            out.println("<a href='javascript:void(0)' class='dropbtn'>Lister</a>");
            out.println("<div class='dropdown-content'>");
            out.println("<a href='BrukerListe'>Brukerliste</a>");
            out.println("<a href='ModulListe'>Modulliste</a>");
            out.println("</div>");
            out.println("<li class='dropdown'>");
            out.println("<a href='javascript:void(0)' class='dropbtn'>Kurs</a>");
            out.println("<div class='dropdown-content'>");
            out.println("<a href='#'>IS-200</a>");
            out.println("<a href='#'>IS-201</a>");
            out.println("<a href='#'>IS-202</a>");
            out.println("</div>");
            out.println("<li style='float:right'><a href='#about'>Innstillinger</a></li>");
            out.println("</ul>");
            out.println("</div>");
        }
}
