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
                 
            HttpSession session = request.getSession();
            boolean isForeleser = (boolean)session.getAttribute("isForeleser");
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta default-character-set='utf8'/>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleNavbar.css'>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleBody.css'>");            
            out.println("<title>Legg til bruker</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<form name='BrukerListe' action='BrukerListe' id='LeggTilBruker' method='post'>");
            
            //Query, resultset og variabler som skal brukes
            Query query = new Query();
            ResultSet rs = null;
            String fornavn = "";
            String etternavn = "";
            String email = "";
            String tlf = "";
            String id = request.getParameter("id");
            
            if(id!= null) {
                /* Hvis id parameteren inneholder noe (ikke lik null) har det blitt trykket på en 
                 * bruker i BrukerListe slik at informasjon om brukeren kommer opp i feltene
                 * + valg mellom oppdater bruker og slett bruker
                 */
                rs = query.query("select * from foreleser where id = "+id+" union select * from student where id = "+id);
                rs.next();
                fornavn = rs.getString(2);
                etternavn = rs.getString(3);
                email = rs.getString(4);
                tlf = rs.getString(5);
                

                out.println("Brukerid <input type='text' name='id' value='"+id+"' readonly><br>");
                printFelter(Fornavn,Etternavn,Email,Tlf,out);
                if(isForeleser){
                out.println("<input type='submit' name='button' value='Oppdater bruker'>");
                out.println("<input type='submit' name='button' value='Slett bruker'>");
                }
                out.println("<input type='submit' name='button' value='Gå tilbake'>");

            } else {
                //Hvis det er trykket på legg til bruker knappen skal tomme felter + radio knapper vises
                printFelter(fornavn,etternavn,email,tlf,out);
                out.println("<input type='radio' name='brukerType' value='student' checked> Student<br>");
                out.println("<input type='radio' name='brukerType' value='foreleser'> Foreleser<br>");
                out.println("<input type='submit' name='button' value='Legg til'>");
            }
            
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
            query.close();
        } catch (SQLException ex){
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Felles metode for input feltene på siden
    public void printFelter (String fornavn, String etternavn, String email, String tlf, PrintWriter out) {
        out.println("Fornavn <input type='text' name='fornavn' value='"+fornavn+"'><br>");
        out.println("Etternavn <input type='text' name='etternavn' value='"+etternavn+"'><br>");
        out.println("Email <input type='text' name='email' value='"+email+"'><br>");
        out.println("Tlf <input type='number' maxlength='8' name='tlf' Value='"+tlf+"'><br>");
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
