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
 * @author Erlend Thorsen
 */
@WebServlet(name = "BrukerListeKurs", urlPatterns = {"/BrukerListeKurs"})
public class BrukerListeKurs extends HttpServlet {

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
            BrukerListe bl = new BrukerListe();
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title></title>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleBody.css'>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleNavbar.css'>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleLeftSidebar.css'>");
            out.println("</head>");
            out.println("<body>");
            
            HttpSession session = request.getSession();
            String kursId = request.getParameter("kursId");
            Navbar navbar = new Navbar();
            boolean isForeleser = (boolean)session.getAttribute("isForeleser");
            String redigerBrukere =  request.getParameter("redigerBrukere");

            

            out.println("<div class='mainContent'>");
            
            if(!redigerBrukere.equals("true")){
                //Skriver ut liste over studenter og forelesere
                String foreleser = ("SELECT A.fornavn, A.etterNavn, A.id from Foreleser A where  A.id in ( select B.foreleserId from ForeleserKurs B where B.kursId ='"+kursId+"')");
                String student = ("SELECT A.fornavn, A.etterNavn, A.id from Student A where  A.id in ( select B.studentId from TarKurs B where B.kursId ='"+kursId+"')");

                //Forelesere
                out.println("<h2>Studenter og forelesere som tar " +kursId+ "</h2>");
                out.println("<b>Forelesere:</b>");

                bl.skrivListe(foreleser, rs, query, out);

                //Studenter
                out.println("<b>Studenter:</b>");
                bl.skrivListe(student, rs, query, out);
                if(isForeleser){
                    out.println("<button type='submit' onclick=\"window.location.href='BrukerListeKurs?kursId="+kursId+"&redigerBrukere=true'\">Legg til/Fjern brukere</button>");
                }
            } else{
                out.println("<h2>Legg til eller fjern brukere fra "+kursId+"</h2>");
                
            }
           

            out.println("</div>");
            //printer sidebar og navbar
            navbar.printLeftSidebar("Personer", kursId, out);
            try {
                navbar.printNavbar("Kurs",(String)session.getAttribute("id"),(boolean)session.getAttribute("isForeleser"), out);
            } catch (SQLException ex) {
                Logger.getLogger(Kurs.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.println("</body>");
            out.println("</html>");
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
