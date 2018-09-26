/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Database.connectToDatabase;
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

/**
 *
 * @author vegar
 */
@WebServlet(name = "LeggTilBruker", urlPatterns = {"/LeggTilBruker"})
public class LeggTilBruker extends HttpServlet {

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
            //out.println("<link rel='stylesheet' type='text/css' href='style/styleNavbar.css'");
            //out.println("<link rel='stylesheet' type='text/css' href='style/styleBody.css'");            
            out.println("<title>Legg til bruker</title>");            
            out.println("</head>");
            out.println("<body>");
            
            out.println("<h1>Servlet LeggTilBruker at " + request.getContextPath() + "</h1>");
            out.println("<form name='BrukerListe' action='BrukerListe' id='LeggTilBruker' method='post'>");
            
            connectToDatabase ctd = new connectToDatabase();
            ctd.init();
            Connection con = ctd.getConnection();
            ResultSet rs = null;
            String Fornavn = "";
            String Etternavn = "";
            String Email = "";
            String Tlf = "";
            
            if(request.getParameter("id")!= null) {
                String id = request.getParameter("id");
                String DataString = ("select * from foreleser where id = "+id+" union select * from student where id = "+id);
                PreparedStatement HentData = con.prepareStatement(DataString);
                rs = HentData.executeQuery();
                rs.next();
                Fornavn = rs.getString(2);
                Etternavn = rs.getString(3);
                Email = rs.getString(4);
                Tlf = rs.getString(5);
                
                out.println("Brukerid <input type='text' name='id' value='"+request.getParameter("id")+"' readonly><br>");
                printFelter(Fornavn,Etternavn,Email,Tlf,out);
                out.println("<input type='submit' name='button' value='oppdater bruker'>");
                out.println("<input type='submit' name='button' value='slett bruker'>");
            } else {
                printFelter(Fornavn,Etternavn,Email,Tlf,out);
                out.println("<input type='radio' name='brukertype' value='student' checked> Student<br>");
                out.println("<input type='radio' name='brukertype' value='foreleser'> Foreleser<br>");
                out.println("<input type='submit' name='button' value='legg til'>");
            }
            
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        } catch (SQLException ex){
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void printFelter (String Fornavn, String Etternavn, String Email, String Tlf, PrintWriter out) {
        out.println("Fornavn <input type='text' name='Fornavn' value='"+Fornavn+"'><br>");
        out.println("Etternavn <input type='text' name='Etternavn' value='"+Etternavn+"'><br>");
        out.println("Email <input type='text' name='Email' value='"+Email+"'><br>");
        out.println("Tlf <input type='text' name='Tlf' Value='"+Tlf+"'><br>");
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