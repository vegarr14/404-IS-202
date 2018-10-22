/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Database.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author felpus
 */
@WebServlet(name = "GruppeListe", urlPatterns = {"/GruppeListe"})
public class GruppeListe extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            ResultSet rs = null;
            Query query = new Query();
            request.setCharacterEncoding("utf8");
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleNavbar.css'>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleBody.css'>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleLeftSidebar.css'>");  
            out.println("<title>Grupper</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OpprettGrupper at " + request.getContextPath() + "</h1>");
            HttpSession session = request.getSession();
            String kursId = request.getParameter("kursId");
            out.println(kursId);
            Navbar navbar = new Navbar();
            
            navbar.printLeftSidebar("Grupper", kursId, out);
                try {
                navbar.printNavbar("Kurs",(String)session.getAttribute("id"),(boolean)session.getAttribute("isForeleser"), out);
            } catch (SQLException ex) {
                Logger.getLogger(Kurs.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                if(request.getParameter("button") != null) {
                    
                    if(request.getParameter("button").equals("opprett")) {
                        
                        String Gruppenavn = request.getParameter("Gruppenavn");
                        
                        query.update("INSERT into Gruppe (gruppenavn,gruppeSkaper) Values ('"+Gruppenavn+"','"+session.getAttribute("id")+"')");
                        rs = query.query("SELECT gruppe_id FROM Gruppe where gruppenavn='"+Gruppenavn+"'");
                        rs.next();
                        int gruppeid = rs.getInt(1);
                        query.update("INSERT into Gruppetilkurs Values ('"+kursId+"',"+gruppeid+")");
                        
                    } else if (request.getParameter("button").equals("endre gruppenavn")) {
                        String Gruppenavn = request.getParameter("Gruppenavn");
                        String gruppeid = request.getParameter("gruppeid");
                        query.update("UPDATE gruppe set gruppenavn ='"+Gruppenavn+"' where gruppe_id ='"+gruppeid+"'");
                        
                    } 
                    else if (request.getParameter("button").equals("slett gruppe")) {
                        query.update("DELETE from gruppetilbruker where gruppe_id = "+request.getParameter("gruppeid"));
                        query.update("DELETE from Gruppetilkurs where kursId = '"+kursId+"' AND gruppe_id = "+request.getParameter("gruppeid"));
                        query.update("DELETE from gruppe where gruppe_id = "+request.getParameter("gruppeid"));
                    }
                    else if (request.getParameter("button").equals("bli medlem")) {
                        
                        query.update("INSERT INTO Gruppetilbruker (id,gruppe_id) values ('"+session.getAttribute("id")+"','"+request.getParameter("gruppeid")+"')");
                        
                    }
                    else if (request.getParameter("button").equals("forlat gruppe")) {
                        
                        query.update("DELETE FROM Gruppetilbruker WHERE id = "+session.getAttribute("id")+" AND gruppe_id = "+request.getParameter("gruppeid")+""); 
                    }
                }   
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.println("<div class='velkommen'>");
            String gruppekurs = ("SELECT Gruppe.gruppe_id,Gruppe.gruppenavn FROM Gruppe INNER JOIN Gruppetilkurs ON Gruppe.gruppe_id = Gruppetilkurs.gruppe_id WHERE kursId = '"+kursId+"'");
            String gruppe = ("SELECT gruppe_id,gruppenavn FROM Gruppe");
            out.println("<b>Grupper</b>"); 
            if(kursId != null) {
                skrivListe(gruppekurs, kursId, rs, query, out);
            }
            else{
                skrivListe(gruppe, kursId, rs, query, out);
            }
            
            out.println("<form name='OpprettGruppe' action='OpprettGruppe?kursId="+kursId+"' method='post'>");
            out.println("<button type='submit'>Opprett ny gruppe</button>");
            
            out.println("</form>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
            query.close();
        }
    }
    
    public void skrivListe(String statement, String kursId, ResultSet rs, Query query, PrintWriter out) {
        try {
            rs = query.query(statement);
            out.println("<u1>");
            while(rs.next()){
                out.println("<li> <a href ='OpprettGruppe?kursId="+kursId+"&gruppeid="+rs.getString(1)+"'>" + rs.getString(2) + "</a></li>");
            }
            out.println("</u1>");
            rs = null;
            
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
