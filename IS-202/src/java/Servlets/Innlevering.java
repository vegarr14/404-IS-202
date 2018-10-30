/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Database.Query;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 *
 * @author vegar
 */
@WebServlet(name = "Innlevering", urlPatterns = {"/Innlevering"})
@MultipartConfig
public class Innlevering extends HttpServlet {
    byte[] barray;

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
        //Sjekker om det er foreleser eller student som er innlogget
        HttpSession session = request.getSession();
        if ((boolean)session.getAttribute("isForeleser")) {
            isForeleser(request, response, null);
        } else {
            isStudent(request, response, session);
        }
    }
    
    //Viser en innlevering og gir mulighet til å laste ned fil som hører til innlevering
    public void isForeleser(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Innlevering</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Innlevering at " + request.getContextPath() + "</h1>");
            try {
                Query query = new Query();
                ResultSet rs = query.query("select fileName, innlevKommentar, innlevId from Innlevering where innlevId = "+request.getParameter("innlevId"));
                rs.next();
                out.println(rs.getString(2)+"<br>");
                out.println("<a href='Download?innlevId="+rs.getString(3)+"'>" +rs.getString(1)+ "</a>");
                
                
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
            out.println("</body>");
            out.println("</html>");
        }
        
        
    }
    
    //Legger inn innlevering i database
    public void isStudent(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Innlevering</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Innlevering at " + request.getContextPath() + "</h1>");
            
            Query query = new Query();
            
            
            for (Part part : request.getParts()) {
                String name = part.getSubmittedFileName();
                if (name != null && name.length() > 0) {
                    // File data
                    out.println(part.getSubmittedFileName()+ "</br>");
                    out.println(part.getSize()+ "</br>");
                    InputStream is = part.getInputStream();
                    String insert = "insert into Innlevering(fileName, fileData, modulId, id, innlevKommentar) values('"+name+"',?,"+request.getParameter("modulId")+","
                            + (String)session.getAttribute("id")+",'"+request.getParameter("kommentar")+"')";
                    query.insertFile(insert , is);
                }   
            }
            query.close();
            
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
