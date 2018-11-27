/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Database.Query;
import NotifikasjonSystem.subclasses.NyKunngjoringNotifikasjon;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
 * @author filip
 */
@WebServlet(name = "Kunngjøringer", urlPatterns = {"/Kunngjoringer"})
public class Kunngjøringer extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleBody.css'>"); 
            out.println("<link rel='stylesheet' type='text/css' href='style/styleLeftSidebar.css'>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleNavbar.css'>");
            out.println("<title>Servlet Kunngjøringer</title>");            
            out.println("</head>");
            out.println("<body>");
     
            out.println("<div class='mainContent'>");
            out.println("<h1>Kunngjøringer</h1>");
            
            Query query = new Query();
            ResultSet rs = null;
            HttpSession session = request.getSession();
            String kursId = request.getParameter("kursId");
            String skjultKunngjoringId = "";
            
            NyKunngjoringNotifikasjon nyKunnNot = new NyKunngjoringNotifikasjon();

            
            String dato = new SimpleDateFormat("dd-MM-yyyy' klokken 'HH:mm").format(Calendar.getInstance().getTime());
            
            if ((boolean)session.getAttribute("isForeleser")) { 
                out.println("<form name='Kunngjøringer' action=Kunngjoringer?kursId="+kursId+" method='post'>");
                out.println("Ny kunngjøring </br> <textarea cols='100' rows='10' name='kunngjoring' ></textarea></br>");
                out.println("<input type='submit' name='button' value='Opprett'></br>");
                out.println("</form>");        
            }
            
            if(request.getParameter("button") != null) {
                if(request.getParameter("button").equals("Opprett")) {
                    String kunngjøring = request.getParameter("kunngjoring");
                    String id = (String)session.getAttribute("id");
                    query.update("INSERT INTO Kunngjøringer (kunngjøring, kursId, foreleserId, dato) values ('"+kunngjøring+"','"+kursId+"','"+id+"','"+dato+"')");
                    //Skriver ny notifikasjon om det blir opprettt en kunngjøring
                    nyKunnNot.getAndSetNyKungjoring(kursId, Integer.parseInt(id));
                }
                else if(request.getParameter("button").equals("Slett")) {
                    query.update("DELETE FROM Kunngjøringer WHERE kunngjøringId = '"+request.getParameter("skjultKunngjoringId")+"'");
                }
            }
            
            
            String kunngjøringer = ("SELECT kunngjøring, forNavn, etterNavn, dato, kunngjøringId FROM Kunngjøringer \n" +
                    "INNER JOIN foreleser on kunngjøringer.foreleserId = foreleser.id WHERE kursId = '"+kursId+"'\n" +
                    "order by kunngjøringId DESC LIMIT 10 ");
            skrivKunngjøringListe(kunngjøringer, kursId, rs, query, session, out);
            
            try {
                Navbar navbar = new Navbar();
                navbar.printLeftSidebar("Kunngjøringer", request.getParameter("kursId"), out);
                navbar.printNavbar("Kurs", (String)session.getAttribute("id"),(boolean)session.getAttribute("isForeleser"), out); 
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.println("</body>");
            out.println("</html>");
            query.close();
        }
    }
    
    public void skrivKunngjøringListe(String statement, String kursId, ResultSet rs, Query query, HttpSession session, PrintWriter out) {
        try {
            rs = query.query(statement);
            out.println("<u1>");
            while(rs.next()){
                out.println("<div class='bokser'>");
                out.println("<li> " + rs.getString(1) + "</li><div id='bottomInfo'><div class='lowerleft'> <p> " + rs.getString(2) + " " + rs.getString(3) +  "</p></div><div class='lowerright'><p> Skrevet den " + rs.getString(4) + "</p></div></div>"); 
                        
                        if ((boolean)session.getAttribute("isForeleser")) {              
                        out.println("<form name='SlettKunngjøringer' action=Kunngjoringer?kursId="+kursId+" method='post'>");
                        out.println("<div class='middle'> <input type='submit' name='button' value='Slett'></div>"); 
                        out.println("<input type='hidden' name='skjultKunngjoringId' value ='"+rs.getString(5)+"'>");
                        out.println("</form>");
                        }
                        else {
                            out.println("</br>");
                        }
                    out.println("</ul>");
                    out.println("</div>");
            }

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
