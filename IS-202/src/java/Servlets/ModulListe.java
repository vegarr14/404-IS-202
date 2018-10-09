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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sondre
 */
@WebServlet(name = "ModulListe", urlPatterns = {"/ModulListe"})
public class ModulListe extends HttpServlet {

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
            
            /*Lage nytt Query-objekt, resultset ( = null, setter modulListe som PreparedStatement.*/
            Query query = new Query();
            ResultSet rs = null;
            PreparedStatement modulListe;
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ModulListe</title>");
            out.println("<link rel='stylesheet' type='text/css' href='style/modulListe.css'>");            
            out.println("</head>");
            out.println("<body>");
            
            out.println("<h1> Moduler </h1>");
            
                /*Velger alt fra modulListe-table fr>a MySQL og skriverModulliste. Se Query for mer.*/
                out.println("<table name=modulListe>");
                    query.skrivModulliste("SELECT modul_Navn, modul_Nummer, forNavn, etterNavn, Foreleser.id FROM modulListe JOIN Foreleser ON ModulListe.id = Foreleser.id", "modulListe", out);
                out.println("</table>");
            
                //out.println("<button href='LeggTilModul' name='nyModul'>Legg Til Modul</button>");
                //out.println("<button href='Login' class='Tilbake'>Tilbake</button>");   
            
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
