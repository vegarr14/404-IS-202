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
import javax.servlet.http.HttpSession;
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
        request.setCharacterEncoding("UTF8");
        Query query = new Query();
        ResultSet rs = null;
        
        String brukernavn = request.getParameter("brukernavn");
        String passord = request.getParameter("passord");
        
        try {
           
            rs = query.query("SELECT ID FROM bruker WHERE brukernavn ='" + brukernavn + "' AND passord = AES_ENCRYPT('" + passord + "', 'domo arigato mr.roboto') LIMIT 1");
            
            // Om innloggingsinfo er riktig prøv å logg inn
            if(rs.next()){
               
                HttpSession session = request.getSession();
                
                String id = rs.getString(1);
                
                //Sjekker om bruker er foreleser og legger attributter i session
                rs = query.query("Select forNavn, etterNavn from foreleser where id ='" + id + "'");                
                if(rs.next()){
                    session.setAttribute("isForeleser", true);

                //Om bruker ikke er foreleser sjekker om bruker er student og legger attributter i session
                }else if(!rs.next()){
                    rs = query.query("Select forNavn, etterNavn from student where id ='" + id + "'");
                    if(rs.next()){
                        session.setAttribute("isForeleser", false);
                        
                    //Om bruker er hverken student eller forelser sendes den tilbake til loginside med feilmeldin    
                    }else if(!rs.next()){
                        request.setAttribute("missingStatus", "true");
                        RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
                        rd.forward(request, response);
                        
                    }else{
                        System.out.println("Feil i forsøk på å sjekke student tabell");
                    }
                   
                }else{
                    System.out.println("Feil i forsøk på å sjekke forelser tabell");
                }
                
                //Setter atributter
                session.setAttribute("fornavn", rs.getString(1));
                session.setAttribute("etternavn", rs.getString(2));
                session.setAttribute("id", id);
                session.setAttribute("loggedIn", "true");
                
                 //Hvis riktig innlogging send til forside
                RequestDispatcher rd = request.getRequestDispatcher("forside.jsp");
                rd.forward(request, response);
            
            //Ved feil brukernavn eller passord, send tilbake til login.jsp med feilmelding
            }else if(!rs.next()){                
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
          query.close();       
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
