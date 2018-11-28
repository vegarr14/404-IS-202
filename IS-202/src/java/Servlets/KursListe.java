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
import javax.servlet.http.HttpSession;
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
        request.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession();
            
            ResultSet rs = null;
            Query query = new Query();
            
            String kursId = "";
            String kursNavn = "";
            String kursBilde = "";
            String kursTekst = "";
            
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>KursListe</title>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleNavbar.css'>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleBody.css'>");            
            out.println("</head>");
            out.println("<body>");
            
            if(request.getParameter("button") != null) {
                
                kursId = request.getParameter("KursId");
                kursNavn = request.getParameter("KursNavn");
                kursBilde = request.getParameter("KursBilde");
                kursTekst = request.getParameter("KursTekst");
                
                
                if(request.getParameter("button").equals("legg til")) {

                    //Legger til kurs
                    query.update("INSERT INTO kurs (kursId, kursNavn, kursBilde, kursTekst) values('"+kursId+"', '"+kursNavn+"', '"+kursBilde+"','"+kursTekst+"')");
                    query.update("INSERT INTO foreleserKurs (kursId, foreleserId) values('"+kursId+"','"+(String)session.getAttribute("id")+"')");
                } else if (request.getParameter("button").equals("oppdater kurs")) {
                    //Oppdaterer Kurs
                    query.update("UPDATE kurs set kursId ='"+kursId+"',kursNavn ='"+kursNavn+"', kursBilde='"+kursBilde+"', kursTekst='"+kursTekst+"' where kursId='"+kursId+"'");
                } else if (request.getParameter("button").equals("slett kurs")) {
                    //Fjerner Kurs
                    query.update("DELETE from kurs where kursId = '"+request.getParameter("KursId")+"'");
                }
            }
            //Skriver ut liste over kurs

            String kurs = ("SELECT kursnavn,kursId FROM Kurs");
            
            //Kurs
            out.println("<div class='velkommen'>");
            out.println("<b>Kurs:</b>"); 
            try{
            rs = query.query(kurs);
            out.println("<u1>");
            //Skriver ut felt en og to for hver rad i query + setter et felt lik id til bruker som sendes videre hvis noen skal endre informasjonen om en bruker
            while(rs.next()){
                out.println("<li> <a href ='LeggTilKurs?KursId="+rs.getString(2)+"'>" + rs.getString(2) +"</a></li>");
            }
            out.println("</u1>");
            rs = null;
            
            }catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
  
            out.println("<form name='LeggTilKurs' action='LeggTilKurs' method='post'>");
            out.println("<button class='button' type='submit'>Legg til kurs</button>");
            out.println("</form>");
            out.println("</div>");
            
            //Printer Navbar
            try {
                Navbar navbar = new Navbar();
                navbar.printNavbar("Kurs",(String)session.getAttribute("id"),(boolean)session.getAttribute("isForeleser"), out);
            } catch (SQLException ex) {
                Logger.getLogger(Kurs.class.getName()).log(Level.SEVERE, null, ex);
            }
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

