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

/**
 *
 * @author vegar
 */
@WebServlet(name = "BrukerListe", urlPatterns = {"/BrukerListe"})
public class BrukerListe extends HttpServlet {

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
            
            connectToDatabase ctd = new connectToDatabase();
            ctd.init();
            Connection con = ctd.getConnection();
            ResultSet rs = null;
            PreparedStatement studentliste;
            PreparedStatement foreleserliste;
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>BrukerListe</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet BrukerListe at " + request.getContextPath() + "</h1>");
            try {
                if(!request.getParameter("Fornavn").equals("null")) {
                   
                    String Fornavn = request.getParameter("Fornavn");
                    String Etternavn = request.getParameter("Etternavn");
                    String Email = request.getParameter("Email");
                    String Tlf = request.getParameter("Tlf");
                    String Type = request.getParameter("brukertype");
                    
                    String NyBruker = ("INSERT into bruker (brukernavn, passord) Values ('"+Fornavn+"', aes_encrypt('test', 'domo arigato mr.roboto'))");
                    PreparedStatement Leggtilbruker = con.prepareStatement(NyBruker);
                    Leggtilbruker.executeUpdate();
                    
                    PreparedStatement hentverdi = con.prepareStatement("SELECT max(id) FROM Bruker");
                    rs = hentverdi.executeQuery();
                    rs.next();
                    int id = rs.getInt(1);
                    rs = null;                    
                    
                    String LeggTil =("INSERT INTO "+Type+" values('"+id+"','"+Fornavn+"','"+Etternavn+"','"+Email+"','"+Tlf+"')");
                    PreparedStatement leggtilstudent = con.prepareStatement(LeggTil);
                    leggtilstudent.executeUpdate();
                    
                }
            }
            catch (NullPointerException ignore) {
                System.out.println("nullpointer");
            }
            catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            
            try {
                //Forelesere
                out.println("<b>Forelesere:</b>");
                foreleserliste = con.prepareStatement("SELECT forNavn,etterNavn FROM Foreleser");
                rs = foreleserliste.executeQuery();
                //Skriver ut felt en og to for hver rad i query
                out.println("<ul>");
                while(rs.next()){
                    out.println("<li>" + rs.getString(1) + " " + rs.getString(2) + "</li>");
                }
                out.println("</u1>");
                rs = null;
                
                //Srudenter
                out.println("<b>Studenter:</b>");
                studentliste = con.prepareStatement("SELECT forNavn,etterNavn FROM Student");
                rs = studentliste.executeQuery();
                //Skriver ut felt en og to for hver rad i query
                out.println("<u1>");
                while(rs.next()){
                    out.println("<li>" + rs.getString(1) + " " + rs.getString(2) + "</li>");
                }
                rs = null;
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.println("</u1>");
            out.println("<form name='LeggTilBruker' action='LeggTilBruker' method='post'>");
            out.println("<button type='submit'>Legg til bruker</button>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
            ctd.close(rs);
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
