/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Database.connectToDatabase;
//import com.sun.istack.internal.logging.Logger;
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

/**
 *
 * @author Erlend Thorsen
 */
@WebServlet(name = "signupComplete", urlPatterns = {"/signupComplete"})
public class signupComplete extends HttpServlet {

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
        
        connectToDatabase ctd = new connectToDatabase();
        ctd.init();
        Connection con = ctd.getConnection();
        
        String fornavn = request.getParameter("fornavn");
        String etternavn = request.getParameter("etternavn");
        String email = request.getParameter("email");
        String tlf = request.getParameter("tlf");
        String brukernavn = request.getParameter("brukernavn");
        String passord = request.getParameter("passord");
        String s = "ok 1";
        
        ResultSet rs = null;
        //PreparedStatement checkUserAvailable;
        try{
            PreparedStatement checkUserAvailable = con.prepareStatement("SELECT ID FROM innlogginsinfo WHERE brukernavn = '" + brukernavn + "'");
            rs = checkUserAvailable.executeQuery();
            s = "ok 2";
            if(rs.next()){
                s = "Brukernavn er allerede i bruk";
            }else if(!rs.next()){
                s ="ok før her";
                String createKunde = "INSERT INTO kunde(Fornavn,Etternavn) VALUES(?,?)";
                PreparedStatement createUser = con.prepareStatement(createKunde);
                createUser.setString(1, fornavn);
                createUser.setString(2, etternavn);
                s ="ok før her2";
                createUser.executeUpdate();
                s = "ok 3";
                createUser = con.prepareStatement(
                        "INSERT INTO kontaktinfo (ID, Epost, TelefonNummer) VALUES(LAST_INSERT_ID(), '"+ email + "', '" + (Integer.parseInt(tlf)) + "')");
                createUser.executeUpdate();
                s = "ok 4";
                createUser = con.prepareStatement(
                        "INSERT INTO innlogginsinfo (ID, brukernavn, passord) VALUES(LAST_INSERT_ID(), '"+ brukernavn + "', '" + passord + "')");
                createUser.executeUpdate();
                s = "Gratulerer du har laget en bruker";
             
            }else{
                s = "there something happening here!";
            }
            
        } catch (SQLException ex){
            //s = "wrong";
            System.out.println(ex.getMessage());
            java.util.logging.Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        rs = null;
        ctd.close(rs);
        
                
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet signup</title>");
            out.println("<link rel='stylesheet' type='text/css' href='style.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>"+s+"</h1>");
            out.println("<form action=/hello2/index.html>");
            out.println("<input type='submit' value='Gå til innlogging'>");
            out.println("</form>");
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
