/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Servlets.Navbar;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author vegar
 */
public class Forside {
    public Forside()
    {}
    
    public void skrivForside(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession();
        
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet presentContactInfo</title>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleNavbar.css'>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleBody.css'>");
            out.println("</head>");
            out.println("<body>");
            Navbar navbar = new Servlets.Navbar();
            //navbar.printNavbar("Hjem", out);
            out.println("<div class='velkommen'>");
            out.println("<h1>Under Construction</h1>");
            out.println("<h1> Velkommen " + session.getAttribute("fornavn") + " " + session.getAttribute("etternavn") + "</h1>");
            out.println("<h2>Din id i systemet er: " + session.getAttribute("id") +"</h2>");
            out.println("<h2>Her kommmer det snart mye gøy<br>Vennligst sjekk igjen senere</h2>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
