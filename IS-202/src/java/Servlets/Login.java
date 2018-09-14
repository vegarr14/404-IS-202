/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Database.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
/**
 *
 * @author Erlend Thorsen
 */
@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

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
        //Kobler til database
        connectToDatabase ctd = new connectToDatabase();
        ctd.init();
        Connection con = ctd.getConnection();
        
        String brukernavn = request.getParameter("brukernavn");
        String passord = request.getParameter("passord");
        
        ResultSet rs = null;
        PreparedStatement checklogon;
        
        try {
           
            checklogon = con.prepareStatement("SELECT ID FROM bruker WHERE brukernavn = ? AND passord = AES_ENCRYPT(?, 'domo arigato mr.roboto') LIMIT 1");
            checklogon.setString(1, brukernavn);
            checklogon.setString(2, passord);
            rs = checklogon.executeQuery();
            
            if(rs.next()){
                //Hvis riktig innlogging send til forside
                Forside forside = new Forside();
                forside.skrivForside(request, response);
            }else if(!rs.next()){
                //Ved feil brukernavn eller passord, send tilbake med feilmelding
                request.setAttribute("loginResult", "true");
                RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
                rd.forward(request, response);
            }else{

                System.out.println("Feil i innlogging. Dette skal ikke skje");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
          // Lukker database kobling
          rs = null;
          ctd.close(rs);        
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
