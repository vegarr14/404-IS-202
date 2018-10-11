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
 * @author vegar
 */
@WebServlet(name = "BrukerListe", urlPatterns = {"/BrukerListe"})
public class BrukerListe extends HttpServlet {

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
            
            HttpSession session = request.getSession();
            boolean isForeleser = (boolean)session.getAttribute("isForeleser");
            
            ResultSet rs = null;
            Query query = new Query();
            
            request.setCharacterEncoding("utf8");
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleNavbar.css'>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleBody.css'>");    
            out.println("<title>BrukerListe</title>");            
            out.println("</head>");
            out.println("<body>");
            

            try{
                
                Navbar navbar = new Navbar();
                navbar.printNavbar("Brukerliste",(String)session.getAttribute("id"),(boolean)session.getAttribute("isForeleser"), out);
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            
            try {
                
                             
              if(isForeleser){
                if(request.getParameter("button") != null) {
                    //sjekker om en knapp med name button er trykket på for å åpne siden
                    if(request.getParameter("button").equals("legg til")) {
                        //Kjører hvis det skal legges til ny bruker
                        //lager ny bruker og henter id til ny bruker og setter inn i enten foreleser eller student
                        Brukernavn brukernavn = new Brukernavn(request);
                        String Fornavn = request.getParameter("Fornavn");
                        String Etternavn = request.getParameter("Etternavn");
                        String Email = request.getParameter("Email");
                        String Tlf = request.getParameter("Tlf");
                        String Type = request.getParameter("brukertype");
                        
                        query.update("INSERT into bruker (brukernavn, passord) Values ('"+brukernavn.getBrukernavn()+"', aes_encrypt('test', 'domo arigato mr.roboto'))");
                        rs = query.query("SELECT max(id) FROM Bruker");
                        rs.next();
                        int id = rs.getInt(1);
                        rs = null;
                        
                        query.update("INSERT INTO "+Type+" values('"+id+"','"+Fornavn+"','"+Etternavn+"','"+Email+"','"+Tlf+"')");
                        
                    } else if (request.getParameter("button").equals("Oppdater bruker")) {
                        //kjører hvis en bruker skal oppdateres
                        //prøver å oppdatere i både foreleser og student for en spesifikk id, som kun skal finnes i en av tabellene

                        Brukernavn brukernavn = new Brukernavn(request);
                        String forNavn = request.getParameter("Fornavn");
                        String etterNavn = request.getParameter("Etternavn");
                        String email = request.getParameter("Email");
                        String tlf = request.getParameter("Tlf");
                        String id = request.getParameter("id");
                        String oppdatertforNavn;
                        String oppdatertetterNavn;
                        query.update("UPDATE foreleser set forNavn ='"+forNavn+"',etterNavn='"+etterNavn+"',email ='"+email+"', tlf ='"+tlf+"' where id ='"+id+"'");
                        query.update("UPDATE student set forNavn ='"+forNavn+"',etterNavn='"+etterNavn+"',email ='"+email+"', tlf ='"+tlf+"' where id ='"+id+"'");
                        query.update("UPDATE bruker set brukerNavn ='"+brukernavn.getBrukernavn()+"' where id ='"+id+"'");
                        
                        rs = query.query("Select forNavn, etterNavn from foreleser where id ='" + session.getAttribute("id") + "'");                
                        if(rs.next()){
                            String OppdatertforNavn = rs.getString(1);
                            String OppdatertetterNavn = rs.getString(2);
                            session.setAttribute("fornavn", OppdatertforNavn);
                            session.setAttribute("etternavn", OppdatertetterNavn);
                            rs = null;
                        }
                        rs = query.query("Select forNavn, etterNavn from student where id ='" + session.getAttribute("id") + "'");                 
                        if (rs.next()){
                            String OppdatertforNavn = rs.getString(1);
                            String OppdatertetterNavn = rs.getString(2);
                            session.setAttribute("fornavn", OppdatertforNavn);
                            session.setAttribute("etternavn", OppdatertetterNavn);
                            rs = null;
                        }                                        
                        
                    } else if (request.getParameter("button").equals("Slett bruker")) {
                        //kjører hvis en bruker skal slettes
                        //sletter fra både student og foreleser selv om kun en av de ikke gjør noe
                        query.update("DELETE from foreleser where id = "+request.getParameter("id"));
                        query.update("DELETE from student where id = "+request.getParameter("id"));
                        query.update("DELETE from bruker where id = "+request.getParameter("id"));
                    }else if (request.getParameter("button").equals("Gå tilbake")) {
                        //går tilbake fra LeggTilBruker til Brukerliste, uten å endre noe
                        }
                   } 
                }
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.println("<div class='velkommen'>");
            
            //Skriver ut liste over studenter og forelesere
            String foreleser = ("SELECT forNavn,etterNavn,id FROM Foreleser");
            String student = ("SELECT forNavn,etterNavn,id FROM Student");
            
            //Forelesere
            out.println("<b>Forelesere:</b>"); 
            skrivListe(foreleser, rs, query, out);
              
            //Srudenter
            out.println("<b>Studenter:</b>");
            skrivListe(student, rs, query, out);

            out.println("<form name='LeggTilBruker' action='LeggTilBruker' method='post'>");
            if(isForeleser){
                out.println("<button type='submit'>Legg til bruker</button>");
            }
            out.println("</form>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
            query.close();
        }
    }
    
    public void skrivListe(String statement, ResultSet rs, Query query, PrintWriter out) {
        try {
            rs = query.query(statement);
            out.println("<u1>");
            //Skriver ut felt en og to for hver rad i query + setter et felt lik id til bruker som sendes videre hvis noen skal endre informasjonen om en bruker
            while(rs.next()){
                out.println("<li> <a href ='LeggTilBruker?id="+rs.getString(3)+"'>" + rs.getString(3) +". "+ rs.getString(1) + " " + rs.getString(2) + "</a></li>");
            }
            out.println("</u1>");
            rs = null;
            
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
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
