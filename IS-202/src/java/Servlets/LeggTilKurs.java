/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;


import Database.Query;
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
import javax.servlet.http.HttpSession;
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
            
            HttpSession session = request.getSession();

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Legg Til Kurs</title>"); 
            out.println("<link rel='stylesheet' type='text/css' href='style/styleNavbar.css'>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleBody.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='velkommen'>");
            out.println("<form name='KursListe' action='KursListe' id='LeggTilKurs' method='post'>");          
            
            Query query = new Query();
            ResultSet rs = null;
            String kursId = "";
            String kursNavn = "";
            String kursBilde = "";
            String kursTekst = "";


            
            if(request.getParameter("KursId")!= null) {     
                kursId = request.getParameter("KursId");
                String dataString = ("select * from kurs where kursId = '"+kursId+"'");
                rs = query.query(dataString);
                rs.next();

                kursId = rs.getString(1);
                kursNavn = rs.getString(2);
                kursBilde = rs.getString(3);
                kursTekst = rs.getString(4);

                printFelter(kursId,kursNavn, kursBilde, kursTekst, out);
                out.println("<input type='submit' name='button' value='oppdater kurs'>");
                out.println("<input type='submit' name='button' value='slett kurs'>");
            } else {
                printFelter(kursId,kursNavn, kursBilde, kursTekst, out);

                out.println("<input type='submit' name='button' value='legg til'>");
                }
            out.println("</form>");
            out.println("</div>");
            
            try {
                Navbar navbar = new Navbar();
                navbar.printNavbar("Kurs",(String)session.getAttribute("id"),(boolean)session.getAttribute("isForeleser"), out);
            } catch (SQLException ex) {
                Logger.getLogger(Kurs.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.println("</body>");
            out.println("</html>");
            query.close();
       
        }catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    

    public void printFelter (String kursId, String kursNavn, String kursBilde, String kursTekst, PrintWriter out) {
     out.println("Kursid<br><input type='text' name='KursId' value='"+kursId+"'><br>");
     out.println("Kursnavn<br><input type='text' name='KursNavn' value='"+kursNavn+"'><br>");
     out.println("Link til Bilde<br><input type='text' name='KursBilde' value='"+kursBilde+"'><br>");
     out.println("Beskrivelse av kurset<br><textarea cols='40' rows='5' name='KursTekst'>"+kursTekst+"</textarea><br>");

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
