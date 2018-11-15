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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Erlend Thorsen
 */
@WebServlet(name = "NotifikasjonVideresender", urlPatterns = {"/NotifikasjonVideresender"})
public class NotifikasjonVideresender extends HttpServlet {

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
        
        ResultSet rs = null;
        Query query = new Query();
        
       String id = request.getParameter("id");
       String notId = request.getParameter("notId");
       String notType = request.getParameter("notType");
       String notRefId = request.getParameter("notRefId");
       
       String link = "";
       
       
       try {
       //Sender til riktig modul om typen er nyModul eller oppdatertModul eller slettetModul
        if(notType.equals("nyModul") | notType.equals("oppdatertModul") | notType.equals("slettetModul") | notType.equals("24hInnlevFrist")){

            if(notType.equals("slettetModul")){
             link = "#";
            }else{
                String kursId ="";

                rs = query.query("Select kursId from modul where modulId="+notRefId+"");
                rs.next();
                kursId = rs.getString(1);
      
                 link = "Modul?kursId="+kursId+"&modulId="+notRefId+"";
                
            }
        }
        else if(notType.equals("nyInnlevering")){
            link = "Innlevering?innlevId="+notRefId;
        }
        //sender en tom link altså fortsatt på samme side
        else if(notType.equals("fjernetFraKurs")){
            link = "#";
        }
        else if(notType.equals("lagtTilKurs")){
            link = "Kurs?kursId=" +notRefId;
        }
        
       } catch (SQLException ex) {
             Logger.getLogger(NotifikasjonVideresender.class.getName()).log(Level.SEVERE, null, ex);
       }
       
       //Update notifikasjoner og Close query nice nice nice
       //Setter notifikasjonen som lest
       query.update("UPDATE notifikasjoner SET notUlest=0 WHERE notId="+notId+"");
       query.close();
       //Sender redirect
       response.sendRedirect(link);
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
