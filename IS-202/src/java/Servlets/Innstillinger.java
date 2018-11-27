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
@WebServlet(name = "Innstillinger", urlPatterns = {"/Innstillinger"})
public class Innstillinger extends HttpServlet {

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
            
            Navbar navbar = new Navbar();
            HttpSession session = request.getSession();
            String edit = request.getParameter("edit");
            
            String id = (String)session.getAttribute("id");
            boolean isForeleser = (boolean)session.getAttribute("isForeleser");

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>SLIT - Innstillinger</title>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleBody.css'>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleNavbar.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class=velkommen>");
            out.println("<h1>Endre innstillinger</h1>");
            
            //Sjekker om man skal få opp innstillingsmenyen
            if(edit.equals("false")){
                
                out.println("<form name='velgeForm' action='Innstillinger?edit=true' method='POST'>");
                out.println("<input class='button' type='submit'  name='endrePassord' value='Endre Passord'>");
                out.println("<br>");
                out.println("<input class='button' type='submit' name='endreKontaktInfo' value='Endre Kontaktinformasjon'>");
                out.println("</form>");
            }
            else if(edit.equals("true")){
                //Endrer passord
                if(request.getParameter("endrePassord") != null){
                            
                    out.println("<form name='passordForm' action='EndreInnstillinger?endre=passord' method='POST'>");
                    out.println("<label>Nytt Passord</label>");
                    out.println("<input class='field' type='password' name='nyttPassord'>");
                    out.println("<br>");
                    out.println("<input class='button' type='submit'  name='endrePassord' value='Endre Passord'>");
                                       
                }
                //endrer kontaktinformasjon Kjører sql query
                else if(request.getParameter("endreKontaktInfo") != null){
                    String table = "student";
                    if(isForeleser){
                        table = "foreleser";
                    }
        
                    rs = query.query("SELECT email, tlf from "+table+" where id='"+id+"'");
                    
                    try {
                        rs.next();
                        out.println("<form name='kontaktForm' action='EndreInnstillinger?endre=kontaktInfo' method='POST'>");
                        out.println("<label>E-post</label>");
                        out.println("<input type='text' name='epost' value='"+rs.getString(1)+"'>");    //Bruker ikke type email fordi vi ønsker å kunne bruke æøå
                        out.println("<br>");
                        out.println("<label>Telefon nummer</label>");
                        out.println("<input type='number' name='tlf'value='"+rs.getString(2)+"'>");
                        out.println("<br>");
                        out.println("<input class='button' type='submit'  name='endreKontaktInfo' value='Endre kontaktinformasjon'>");

                    } catch (SQLException ex) {
                        Logger.getLogger(Innstillinger.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    query.close();
                    
                }
                //legges til på alle innstillingene
                out.println("<br>");
                out.println("<input class='button' type='submit' onclick='window.location.href=\"Innstillinger?edit=true\"' value='Gå tilbake'>");
                out.println("</form>"); 
            }
            out.println("</div>");
            
            //Printer navbar
            try {
                navbar.printNavbar("Innstillinger",(String)session.getAttribute("id"),(boolean)session.getAttribute("isForeleser"), out);
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
