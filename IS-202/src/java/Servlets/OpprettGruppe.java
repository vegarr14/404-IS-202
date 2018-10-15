/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Database.connectToDatabase;
import Database.Query;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

/**
 *
 * @author felpus
 */
@WebServlet(name = "OpprettGruppe", urlPatterns = {"/OpprettGruppe"})
public class OpprettGruppe extends HttpServlet {

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
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta default-character-set='utf8'/>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleNavbar.css'>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleBody.css'>");            
            out.println("<title>Opprett grupper</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OpprettGruppe at " + request.getContextPath() + "</h1>");
            out.println("<form name='GruppeListe' action='GruppeListe' id='OpprettGruppe' method='post'>");
            
            HttpSession session = request.getSession();
            Query query = new Query();
            ResultSet rs = null;
            String Gruppenavn = "";
            
            if(request.getParameter("gruppeid")!= null) {
                String gruppeid = request.getParameter("gruppeid");
                rs = query.query("select * from gruppe where gruppe_id = "+gruppeid+"");
                rs.next();
                Gruppenavn = rs.getString(2);
                rs = null;
                out.println("Gruppeid <input type='text' name='gruppeid' value='"+request.getParameter("gruppeid")+"' readonly><br>");
                rs = query.query("SELECT Student.id FROM Student INNER JOIN Gruppe ON Student.id = Gruppe.gruppeSkaper WHERE gruppe_id ="+gruppeid+" AND Gruppe.gruppeSkaper = "+session.getAttribute("id")+"");
                try {
                    if (rs.next()){
                        printFelter(Gruppenavn,out);
                        rs = null;
                    }
                    else printFelterReadonly(Gruppenavn,out);
                } catch (SQLException ignore) {   
                }  
                
                out.println("<b>Medlemmer:</b>"); 
                
                String medlemmer =("SELECT Student.forNavn, Student.etterNavn FROM Student INNER JOIN Gruppetilbruker ON Student.id = Gruppetilbruker.id WHERE gruppe_id ="+gruppeid+"");
                skrivMedlemsListe(medlemmer, rs, query, out); 
                             
                rs = query.query("SELECT Student.id FROM Student INNER JOIN Gruppe ON Student.id = Gruppe.gruppeSkaper WHERE gruppe_id ="+gruppeid+" AND Gruppe.gruppeSkaper = "+session.getAttribute("id")+"");
                try {
                    if (rs.next()){
                        out.println("<input type='submit' name='button' value='endre gruppenavn'>");
                        rs = null;
                    }
                } catch (SQLException ignore) {   
                }
                
                rs = query.query("SELECT Student.id FROM Student INNER JOIN Gruppe ON Student.id = Gruppe.gruppeSkaper WHERE gruppe_id ="+gruppeid+" AND Gruppe.gruppeSkaper = "+session.getAttribute("id")+"");
                try {
                    if (rs.next()){
                        out.println("<input type='submit' name='button' value='slett gruppe'>");
                        rs = null;
                    }
                } catch (SQLException ignore) {   
                } 
                  
                rs = query.query("SELECT Student.id FROM Student INNER JOIN Gruppetilbruker ON Student.id = Gruppetilbruker.id WHERE gruppe_id ="+gruppeid+" AND Gruppetilbruker.id = "+session.getAttribute("id")+"");
                try {
                    if (rs.next()){
                        out.println("<input type='submit' name='button' value='forlat gruppe'>");
                        rs = null;                
                    }
                    else {
                        out.println("<input type='submit' name='button' value='bli medlem'>");              
                    }
                } catch (SQLException ignore) {
                }
                
            } else {
             printFelter(Gruppenavn,out);
             out.println("<input type='submit' name='button' value='opprett'>");
            }
            
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
            query.close();
        } catch (SQLException ex){
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void printFelter (String Gruppenavn, PrintWriter out) {
        out.println("Gruppenavn <input type='text' name='Gruppenavn' value='"+Gruppenavn+"'><br>");
    } 
    public void printFelterReadonly (String Gruppenavn, PrintWriter out) {
    out.println("Gruppenavn <input type='text' name='Gruppenavn' value='"+Gruppenavn+"'readonly><br>");
    }
    
    public void skrivMedlemsListe(String statement, ResultSet rs, Query query, PrintWriter out) {
        try {
            rs = query.query(statement);
            out.println("<u1>");
            while(rs.next()){
                out.println("<li>" + rs.getString(1) +" "+ rs.getString(2) + "</a></li>");
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
