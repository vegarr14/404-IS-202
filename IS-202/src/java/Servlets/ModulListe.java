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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            
            /*Lage nytt Query-objekt, resultset ( = null, setter modulListe som PreparedStatement.*/
            Query query = new Query();
            ResultSet rs = null;
            PreparedStatement modulListe;
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ModulListe</title>");
            out.println("<link rel='stylesheet' type='text/css' href='style/modulListe.css'>");            
            out.println("</head>");
            out.println("<body>");
            
            out.println("<h1> Moduler </h1>");
            
                /*Velger alt fra modulListe-table fr>a MySQL og skriverModulliste. Se Query for mer.*/
                out.println("<table name=modulListe>");
                    query.skrivModulliste("SELECT forNavn, etterNavn, innlev_Id, innlev_Poeng, innlevering.id, ModulListe.modul_Nummer FROM Innlevering join Student join ModulListe where Innlevering.id = Student.id and ModulListe.modul_Id = Innlevering.modul_Id order by Innlevering.id, modul_Nummer", out);
                out.println("</table>");
            
                out.println("<button href='LeggTilModul' name='nyModul'>Legg Til Modul</button>");
                //out.println("<button href='Login' class='Tilbake'>Tilbake</button>");   
                
             try {
                if(request.getParameter("button") != null) {
                    //sjekker om en knapp med name button er trykket på for å åpne siden
                    if(request.getParameter("button").equals("legg til")) {
                        //Kjører hvis det skal legges til ny bruker
                        //lager ny bruker og henter id til ny bruker og setter inn i enten foreleser eller student
                        Brukernavn brukernavn = new Brukernavn(request);
                        String modulNavn = request.getParameter("modulNavn");
                        String modulNummer = request.getParameter("modulNummer");
                        
                        query.update("INSERT into modul (modul_Navn, modul_Nummer) Values ('"+brukernavn.getBrukernavn()+"', aes_encrypt('test', 'domo arigato mr.roboto'))");
                        rs = query.query("SELECT max(id) FROM Bruker");
                        rs.next();
                        int id = rs.getInt(1);
                        rs = null;
                        
                        query.update("INSERT INTO Modul values('"+id+"','"+modulNavn+"','"+modulNummer+"')");
                        
                    } else if (request.getParameter("button").equals("oppdater bruker")) {
                        //kjører hvis en bruker skal oppdateres
                        //prøver å oppdatere i både foreleser og student for en spesifikk id, som kun skal finnes i en av tabellene
                        Brukernavn brukernavn = new Brukernavn(request);
                        String forNavn = request.getParameter("Fornavn");
                        String etterNavn = request.getParameter("Etternavn");
                        String email = request.getParameter("Email");
                        String tlf = request.getParameter("Tlf");
                        String id = request.getParameter("id");
                        query.update("UPDATE foreleser set forNavn ='"+forNavn+"',etterNavn='"+etterNavn+"',email ='"+email+"', tlf ='"+tlf+"' where id ='"+id+"'");
                        query.update("UPDATE student set forNavn ='"+forNavn+"',etterNavn='"+etterNavn+"',email ='"+email+"', tlf ='"+tlf+"' where id ='"+id+"'");
                        query.update("UPDATE bruker set brukerNavn ='"+brukernavn.getBrukernavn()+"' where id ='"+id+"'");
                    } else if (request.getParameter("button").equals("slett bruker")) {
                        //kjører hvis en bruker skal slettes
                        //sletter fra både student og foreleser selv om kun en av de ikke gjør noe
                        query.update("DELETE from foreleser where id = "+request.getParameter("id"));
                        query.update("DELETE from student where id = "+request.getParameter("id"));
                        query.update("DELETE from bruker where id = "+request.getParameter("id"));
                    }
                    
                    
                }
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
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
