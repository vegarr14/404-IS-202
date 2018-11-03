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

/**
 *
 * @author Sondre
 */
@WebServlet(name = "ModulListe", urlPatterns = {"/ModulListe"})
public class ModulListe extends HttpServlet {

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
        HttpSession session = request.getSession();
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            
            /*Lage nytt Query-objekt, resultset ( = null, setter modulListe som PreparedStatement.*/
            Query query = new Query();
            ResultSet rs = null;
            
            request.setCharacterEncoding("utf8");
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Moduler</title>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleNavbar.css'>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleBody.css'>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleLeftSidebar.css'>");
            out.println("</head>");
            out.println("<body>");
            String kursId = request.getParameter("kursId");
            
            out.println("<div class='mainContent'>");
            out.println("<h2> Moduler i "+kursId+"</h2>");
            
            /*Velger alt fra modulListe-table fr>a MySQL og skriverModulliste. Se Query for mer.*/
            //out.println("<table name=modulListe>");
            //query.skrivModulliste("SELECT forNavn, etterNavn, innlev_Id, innlev_Poeng, innlevering.id, ModulListe.modul_Nummer FROM Innlevering join Student join ModulListe where Innlevering.id = Student.id and ModulListe.modul_Id = Innlevering.modul_Id order by Innlevering.id, modul_Nummer", out);
            //out.println("</table>");
            
            
            //out.println("<button href='Login' class='Tilbake'>Tilbake</button>");   
                
             //try {
                if(request.getParameter("button") != null) {
                    //sjekker om en knapp med name button er trykket på for å åpne siden
                    String modulId = request.getParameter("modulId");
                    String modulNummer = request.getParameter("modulNummer");
                    String foreleserId = request.getParameter("foreleserId");
                    String oppgaveTekst = request.getParameter("oppgaveTekst");
                    String type1 = request.getParameter("oppgaveType");
                    String type2 = "";
                    if(type1 != null && !type1.isEmpty()) {
                        type2 = "1";
                    }   
                    else {
                        type2 = "0";        
                    }
                    if(request.getParameter("button").equals("legg til")) {
                        //Kjører hvis det skal legges til ny modul
                        query.update("INSERT into Modul (kursId, foreleserId, modulNummer, oppgaveTekst, levereSomGruppe) values('"+kursId+"','"+foreleserId+"','"+modulNummer+"','"+oppgaveTekst+"','"+type2+"')");                        
                    } else if (request.getParameter("button").equals("oppdater modul")) {
                        //kjører hvis en modul skal oppdateres
                        
                        query.update("UPDATE Modul set kursId ='"+kursId+"',foreleserId='"+foreleserId+"',modulNummer ='"+modulNummer+"', oppgaveTekst ='"+oppgaveTekst+"' where modulId ='"+modulId+"'");
                        
                    } else if (request.getParameter("button").equals("slett modul")) {
                        //kjører hvis en modul skal slettes
                        
                        query.update("DELETE from Modul where modulId = "+request.getParameter("modulId"));
                        
                    }
                    
                }
            //} //catch (SQLException ex) {
                //Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            //}
            
            try {
                rs = query.query("Select * from Modul where kursId = '"+kursId+"'");
                //System.out.println(kursId);
                out.println("<ul>");
                while(rs.next()){
                    out.println("<li> <a href ='Modul?kursId="+kursId+"&modulId="+rs.getString(1)+"'> Modul "+rs.getString(4)+"</a></li>");
                }
                out.println("</ul>");
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.println("<form name='Modul' action='Modul?kursId="+kursId+"' method='post'>");
            if ((boolean)session.getAttribute("isForeleser")) {
                out.println("<button type='submit'>Legg Til Modul</button>");
            }
            out.println("</form>");
            out.println("</div>");
            try{
                
                Navbar navbar = new Navbar();
                navbar.printLeftSidebar("Moduler", request.getParameter("kursId"), out);
                navbar.printNavbar("Kurs", (String)session.getAttribute("id"),(boolean)session.getAttribute("isForeleser"), out);
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
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
