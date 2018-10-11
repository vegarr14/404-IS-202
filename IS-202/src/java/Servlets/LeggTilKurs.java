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
 * @author Josef
 */
@WebServlet(name = "LeggTilKurs", urlPatterns = {"/LeggTilKurs"})
public class LeggTilKurs extends HttpServlet {

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
            out.println("<title>Legg Til Kurs</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LeggTilKurs at " + request.getContextPath() + "</h1>");            
            out.println("<form name='KursListe' action='KursListe' id='LeggTilKurs' method='post'>");          
            
            connectToDatabase ctd = new connectToDatabase();
            ctd.init();
            Connection con = ctd.getConnection();
            ResultSet rs = null;
            String kursId = "";
            String kursNavn = "";

            
            if(request.getParameter("id")!= null) {     
                String id = request.getParameter("id");
                String dataString = ("select * from kurs where id = '"+id+"'");
                PreparedStatement HentData = con.prepareStatement(dataString);
                rs = HentData.executeQuery();
                rs.next();
                kursId = rs.getString(2);
                kursNavn = rs.getString(3);

                out.println("Antallid <input type='text' name='id' value='"+id+"' readonly><br>"); 
                printFelter(kursId,kursNavn,out);
                out.println("<input type='submit' name='button' value='oppdater kurs'>");
                out.println("<input type='submit' name='button' value='slett kurs'>");
            } else {
                printFelter(kursId,kursNavn,out);
                out.println("<input type='submit' name='button' value='legg til'>");
                }
            out.println("</form>");       
            out.println("</body>");
            out.println("</html>");
       
        }catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void printFelter (String kursId, String kursNavn, PrintWriter out) {
     out.println("Kursid <input type='text' name='Kursid' value='"+kursId+"'><br>");
     out.println("Kursnavn <input type='text' name='Kursnavn' value='"+kursNavn+"'><br>");
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
