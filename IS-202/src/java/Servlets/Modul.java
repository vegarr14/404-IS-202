/* 
 * Det som har med å legge til moduler.
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
@WebServlet(name = "Modul", urlPatterns = {"/Modul"})
public class Modul extends HttpServlet {

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
        //Sjekker om det er foreleser eller student
        HttpSession session = request.getSession();
        if ((boolean)session.getAttribute("isForeleser")) {
            isForeleser(request, response, session);
        } else {
            isStudent(request, response);
        }
    }
    
    //Felter for redigering av modul og lister opp alle studenter som har levert modulen
    public void isForeleser(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleNavbar.css'>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleBody.css'>"); 
            out.println("<title>Servlet Modul</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Modul at " + request.getContextPath() + "</h1>");
            out.println("<form name='ModulListe' action='ModulListe' id='Modul' method='post'>");
            
                /*Velger alt fra modulListe-table fra MySQL og skriverModulliste. Se Query for mer.*/
                /*out.println("<table name=modulListe>");
                    query.skrivModulliste("SELECT * FROM modulListe", out);
                out.println("</table>");*/
                
            Query query = new Query();
            ResultSet rs = null;
            String modulId = request.getParameter("modulId");
            String modulNummer = "";
            String kursId = "";
            String foreleserId = "";
            String oppgaveTekst = "";
            String levereSomGruppe = "";
            
            if(modulId!= null) {
                /* Hvis id parameteren inneholder noe (ikke lik null) har det blitt trykket på en 
                 * modul i ModulListe slik at informasjon om brukeren kommer opp i feltene
                 * + valg mellom oppdater modul og slett modul
                 */
                rs = query.query("select * from Modul where modulId = "+modulId);
                rs.next();
                kursId = rs.getString(2);
                foreleserId = rs.getString(3);
                modulNummer = rs.getString(4);
                oppgaveTekst = rs.getString(5);
                levereSomGruppe = rs.getString(6);
                
                out.println("Modulid <input type='text' name='modulId' value='"+modulId+"' readonly><br>");
                printFelter(kursId,foreleserId,modulNummer,oppgaveTekst,out);
                if (levereSomGruppe.equals("1")){
                out.println("Oppgavetype: Gruppelevering<br>");
                }   
                else {out.println("Oppgavetype: Individuell levering<br>");         
                }
                out.println("<input type='submit' name='button' value='oppdater modul'>");
                out.println("<input type='submit' name='button' value='slett modul'>");
            } else {
                //Hvis det er trykket på legg til bruker knappen skal tomme felter + radio knapper vises
                kursId=request.getParameter("kursId");
                foreleserId = (String)session.getAttribute("id");
                printFelter(kursId,foreleserId,modulNummer,oppgaveTekst,out);
                out.println("<input type='checkbox' name='oppgaveType' value='1'>Gruppelevering<br>");
                out.println("<input type='submit' name='button' value='legg til'>");
            }
            
            out.println("</form>");
            if(modulId!= null) {
                out.println("Innleveringer:<br>");
                // Trenger kanskje en unique composite av modul og gruppeID/ID i Innleveringstable for å unngå dupliserte innleveringer
                // Gruppesystemet trenger også en begrensning som gjør at man kan bare bli medlem i en gruppe per kurs.
                // ^ ellers så kan uheldige ting skje
                if (levereSomGruppe.equals("1")){
                    rs = query.query("select innlevId, gruppeNavn from Innlevering inner join Student inner join Gruppetilkurs inner join gruppe where Innlevering.modulId = "+modulId+" and Innlevering.id = Student.id and gruppe.gruppeId = gruppetilkurs.gruppeId");
                    try {
                    out.println("<ul>"); 
                    while (rs.next()) {
                        out.println("<li> <a href='Innlevering?innlevId="+rs.getString(1)+"'>" +rs.getString(2)+ "</a></li>");
                    }
                    out.println("</ul>");
                    } catch (SQLException ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
                else {
                    rs = query.query("select innlevId, forNavn, etterNavn from Innlevering join Student where Innlevering.modulId = "+modulId+" and Innlevering.id = Student.id");
                    try {
                    out.println("<ul>");
                    while (rs.next()) {
                        out.println("<li> <a href='Innlevering?innlevId="+rs.getString(1)+"'>" +rs.getString(2)+" "+rs.getString(3)+ "</a></li>");              
                    }
                    out.println("</ul>");
                    } catch (SQLException ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
            }
            out.println("</body>");
            out.println("</html>");
            query.close();
        } catch (SQLException ex){
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
            /*    
            out.println("<form name='Modul' action='Modul'>");
            out.println("<input type='submit' value=Oppdater></input>");
            
            rs=null;
            String id = request.getParameter("id");
            
            if (id!=null) {
                String navnet = request.getParameter("id");
                
                rs = query.query("SELECT * from Foreleser WHERE id = "+id+" UNION SELECT * from Student WHERE id = "+id);
                String modulNavn = request.getParameter("modul");
                
            }
            */
        }
    
    //Student kan her levere enn modul
    public void isStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            Query query = new Query();
            String modul = "select forNavn, etterNavn, modulId, kursId, modulNummer, oppgaveTekst, foreleserId, levereSomGruppe from Foreleser join Modul\n" +
                    "on Foreleser.id = Modul.foreleserId and modulId = " + request.getParameter("modulId");
            ResultSet rs = query.query(modul);
            rs.next();
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleNavbar.css'>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleBody.css'>"); 
            out.println("<title>Modul "+rs.getString(5)+"</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Modul "+rs.getString(5)+"</h1> Laget av "+rs.getString(1)+" "+rs.getString(2)+"<br>");
            out.println(rs.getString(6)+"<br><br>");
            HttpSession session = request.getSession();
            String oppgaveType = rs.getString(8);
            if (oppgaveType.equals("1")){
            out.println("Oppgavetype: Gruppelevering<br>");
            //VELGER DEN ELDSTE GRUPPA - DVS LAVESTE GRUPPEID DEN FINNER FOR STUDENTEN OG TILSVARENDE GRUPPENAVN
            String gruppeNavn = "select gruppeNavn from gruppe inner join Student inner join tarkurs where student.id = "+String.valueOf(session.getAttribute("id"))+" and tarkurs.kursId = '"+rs.getString(4)+"'";
            ResultSet rs2 = query.query(gruppeNavn);
            try {
                if (rs2.next()) {
                    out.println("Gjeldende gruppe: "+rs2.getString(1)+"");
                    }
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
            else {out.println("Oppgavetype: Individuell levering");         
            }
            out.println("<form action='Innlevering' method='post' enctype='multipart/form-data'>");
            out.println("<input type='text' name='kommentar' />");
            out.println("<input type='file' name='file' />");
            out.println("<input type='submit' />");
            out.println("<input type='hidden' name='modulId' value='"+rs.getString(3)+"'>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
            query.close();
        } catch (SQLException ex){
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
            
    public void printFelter (String kursId, String foreleserId, String modulNummer, String oppgaveTekst, PrintWriter out) {
        Query query = new Query();
        ResultSet rs = null;
        rs = query.query("SELECT kursId, kursNavn from Kurs");
        
        out.println("kursId <input type='text' name='kursId' value='"+kursId+"' readonly><br>");
        out.println("modulNummer <input type='text' name='modulNummer' value='"+modulNummer+"'><br>");
        out.println("foreleserId <input type='text' name='foreleserId' value='"+foreleserId+"' readonly><br>");
        out.println("oppgaveTekst <input type='text' name='oppgaveTekst' value='"+oppgaveTekst+"'><br>");

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
