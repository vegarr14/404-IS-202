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
import java.sql.SQLException;

/**
 *
 * @author Josef
 */
@WebServlet(name = "KursListe", urlPatterns = {"/KursListe"})
public class KursListe extends HttpServlet {

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
            
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>KursListe</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet KursListe at " + request.getContextPath() + "</h1>");
            
            if(request.getParameter("button") != null) {
                if(request.getParameter("button").equals("legg til")) {
                    String Kursid = request.getParameter("Kursid");
                    String Kursnavn = request.getParameter("Kursnavn");
                    query.update("INSERT INTO kurs (kursnavn, kursid) values('"+Kursnavn+"', '"+Kursid+"')");
                } else if (request.getParameter("button").equals("oppdater kurs")) {
                    String kursid = request.getParameter("Kursid");
                    String kursnavn = request.getParameter("Kursnavn");
                    String id = request.getParameter("id");
                    query.update("UPDATE kurs set kursnavn ='"+kursnavn+"',kursid ='"+kursid+"' where id='"+id+"'");
                } else if (request.getParameter("button").equals("slett kurs")) {
                    query.update("DELETE from kurs where id = '"+request.getParameter("id")+"'");
                }
            }
            //Skriver ut liste over kurs
            String kurs = ("SELECT kursnavn,kursid,id FROM Kurs");
            
            //Kurs
            out.println("<b>Kurs:</b>"); 
            try{
            rs = query.query(kurs);
            out.println("<u1>");
            //Skriver ut felt en og to for hver rad i query + setter et felt lik id til bruker som sendes videre hvis noen skal endre informasjonen om en bruker
            while(rs.next()){
                out.println("<li> <a href ='LeggTilKurs?id="+rs.getString(3)+"'>" + rs.getString(2) +", "+ rs.getString(1) +"</a></li>");
            }
            out.println("</u1>");
            rs = null;
            
        }   catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
  
            out.println("<form name='LeggTilKurs' action='LeggTilKurs' method='post'>");
            out.println("<button type='submit'>Legg til kurs</button>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
            query.close();
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

