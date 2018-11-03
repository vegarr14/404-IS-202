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
@WebServlet(name = "EndreInnstillinger", urlPatterns = {"/EndreInnstillinger"})
public class EndreInnstillinger extends HttpServlet {

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
        request.setCharacterEncoding("UTF8");
        String endre = request.getParameter("endre");
        
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("id");
        boolean isForeleser = (boolean)session.getAttribute("isForeleser");
        
        ResultSet rs = null;
        Query query = new Query();
        
        //returnerer til Innstillingsideside om ingen parametre sendes
        if(endre == null){
            request.getRequestDispatcher("Innstillinger?edit=false").forward(request, response);
        }
        else{
            //endrer passord
            if(endre.equals("passord")){
                String nyttPassord = request.getParameter("nyttPassord");
                query.update("UPDATE Bruker SET passord=aes_encrypt('"+nyttPassord+"', 'domo arigato mr.roboto') WHERE id="+id+"");               
            }
            //Endrer kontaktinfo
            else if(endre.equals("kontaktInfo")){
                String nyEpost = request.getParameter("epost");
                String nyTlf = request.getParameter("tlf");
                String table ="student";
                if(isForeleser){
                    table = "foreleser";
                }
                query.update(("UPDATE "+table+" SET email='"+nyEpost+"', tlf="+nyTlf+" WHERE id="+id+""));
            }
                
            request.getRequestDispatcher("Innstillinger?edit=false").forward(request, response);
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
