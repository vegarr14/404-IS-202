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
        HttpSession session = request.getSession();
        try (PrintWriter out = response.getWriter()) {
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleLeftSidebar.css'>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleNavbar.css'>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleBody.css'>"); 
            out.println("<link rel='stylesheet' type='text/css' href='style/styleKommentarer.css'>");
            out.println("<title>Modul</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='mainContent'>");
            out.println("<div class='modul'>");
            //Sjekker om det er foreleser eller student
            if ((boolean)session.getAttribute("isForeleser")) {
                isForeleser(request, response, session, out);
            } else {
                isStudent(request, response, session, out);
            }
            try {
                Navbar navbar = new Navbar();
                navbar.printLeftSidebar("Moduler", request.getParameter("kursId"), out);
                navbar.printNavbar("Kurs", (String)session.getAttribute("id"),(boolean)session.getAttribute("isForeleser"), out);
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.println("</div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    public void isForeleser(HttpServletRequest request, HttpServletResponse response, HttpSession session, PrintWriter out)
            throws ServletException, IOException {
        try {
            out.println("<h1> Modul </h1>");
            out.println("<form name='ModulListe' action='ModulListe' id='Modul' method='post'>");
            Query query = new Query();
            ResultSet rs = null;
            String modulId = request.getParameter("modulId");
            String modulNummer = "";
            String kursId = "";
            String foreleserId = "";
            String oppgaveTekst = "";
            String levereSomGruppe = "";
            int maxPoeng = 0;

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
                maxPoeng = rs.getInt(7);
                String frist = rs.getString(8);
                if (frist != null) {
                    frist = frist.replace(" ","T");
                    frist = frist.substring(0, frist.length() - 3);
                }
                out.println("<label>Modulid</label> <input type='text' name='modulId' value='"+modulId+"' readonly><br>");
                printFelter(kursId,foreleserId,modulNummer,oppgaveTekst,maxPoeng,out);
                if (levereSomGruppe.equals("1")){
                out.println("Oppgavetype: Gruppelevering<br>");
                }   
                else {out.println("Oppgavetype: Individuell levering<br>");         
                }
                
                out.println("Innleveringsfrist <input type='datetime-local' name='innleveringsFrist' value='"+frist+"'><br>");
                out.println("<input type='submit' class='button' name='button' value='oppdater modul'>");
                out.println("<input type='submit' class='button' name='button' value='slett modul'>");
            } else {
                //Hvis det er trykket på legg til bruker knappen skal tomme felter + radio knapper vises
                kursId=request.getParameter("kursId");
                foreleserId = (String)session.getAttribute("id");
                printFelter(kursId,foreleserId,modulNummer,oppgaveTekst,maxPoeng,out);
                out.println("<input type='checkbox' name='oppgaveType' value='1'>Gruppelevering<br>");
                out.println("Innleveringsfrist <input type='datetime-local' name='innleveringsFrist'><br>");
                out.println("<input type='submit' class='button' name='button' value='legg til'>");
            }
            
            out.println("</form>");
            if(modulId!= null) {
                out.println("Alle innleveringer på denne modulen:<br>");
                // Trenger kanskje en unique composite av modul og gruppeID/ID i Innleveringstable for å unngå dupliserte innleveringer
                // ^ burde ikke kunne ha dupliserte innleveringer fordi defansiv programmering bla bla bla
                // Gruppesystemet trenger også en begrensning som gjør at man kan bare bli medlem i en gruppe per kurs.
                // ^ ellers så det være forvirrende. eldste gruppa i faget for den brukeren som leverer kommer til å telle, ikke bra
                if (levereSomGruppe.equals("1")){
                    rs = query.query(
                            "select innlevId, gruppeNavn\n" +"from Innlevering \n" +
                            "join gruppe \n" +
                            "where Innlevering.modulId = "+modulId+" and Innlevering.gruppeId = gruppe.gruppeId");
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
                        out.println("<li> <a href='Innlevering?kursId="+kursId+"&innlevId="+rs.getString(1)+"'>" +rs.getString(2)+" "+rs.getString(3)+ "</a></li>");              
                    }
                    out.println("</ul>");
                    query.close();
                    } catch (SQLException ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
            }
            
            
        } catch (SQLException ex){
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        }
    
    //Student kan her levere enn modul
    public void isStudent(HttpServletRequest request, HttpServletResponse response, HttpSession session, PrintWriter out)
            throws ServletException, IOException {
        try {
            String kursId = request.getParameter("kursId");
            String modulId = request.getParameter("modulId");
            Query query = new Query();
            String modul = "select forNavn, etterNavn, modulId, kursId, modulNummer, oppgaveTekst, foreleserId, levereSomGruppe, maxPoeng from Foreleser join Modul\n" +
                    "on Foreleser.id = Modul.foreleserId and modulId = " + modulId;

            ResultSet rs = query.query(modul);
            rs.next();
            String oppgaveTekst = rs.getString(6);
            String oppgaveTekstBr = oppgaveTekst.replaceAll("\n", " </br>");
            out.println("<h1>Modul "+rs.getString(5)+"</h1> Laget av "+rs.getString(1)+" "+rs.getString(2)+"<br>");
            out.println(oppgaveTekstBr+"</br></br></br>");
            String oppgaveType = rs.getString(8);
            if (oppgaveType.equals("1")){
            out.println("Oppgavetype: Gruppelevering<br>");
            //VELGER DEN ELDSTE GRUPPA - DVS LAVESTE GRUPPEID DEN FINNER FOR STUDENTEN OG TILSVARENDE GRUPPENAVN
            //String gruppeNavn = "select gruppeNavn from gruppe inner join Student inner join tarkurs where student.id = "+String.valueOf(session.getAttribute("id"))+" and tarkurs.kursId = '"+rs.getString(4)+"'";
            String gruppeNavn = 
                    "select gruppeNavn from gruppe\n" +
                    "inner join Gruppetilbruker ON gruppe.gruppeId = Gruppetilbruker.gruppeId\n" +
                    "inner join Student ON Gruppetilbruker.id = Student.id\n" +
                    "inner join gruppetilkurs ON gruppe.gruppeId = gruppetilkurs.gruppeId\n" +
                    "where student.id = "+String.valueOf(session.getAttribute("id"))+" and gruppetilkurs.kursId = '"+rs.getString(4)+"'";
            
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
            out.println("Filopplasting</br><input type='file' name='file' /></br></br>");
            out.println("Kommentar </br> <textarea cols='50' rows='5' name='kommentar' ></textarea></br>");
            out.println("Maks antall oppnåelige poeng: "+rs.getString(9)+"<br>");
            out.println("<input type='submit' class='button' name='button' value='Lever'/>");
            out.println("<input type='hidden' name='modulId' value='"+rs.getString(3)+"'>");
            out.println("<input type='hidden' name='kursId' value ='"+rs.getString(4)+"'>");
            out.println("</form>");
            out.println("</br>Mine innleveringer på denne modulen:</br>");
            rs = query.query("select innlevId from Innlevering where modulId = "+modulId+" and id = "+(String)session.getAttribute("id"));
            out.println("<ul>");
            while (rs.next()) {
                out.println("<li> <a href='Innlevering?kursId="+kursId+"&innlevId="+rs.getString(1)+"'> Innlevering </a></li>");
            }
            out.println("</ul>");
            query.close();
        } catch (SQLException ex){
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
            
    public void printFelter (String kursId, String foreleserId, String modulNummer, String oppgaveTekst, int maxPoeng, PrintWriter out) {
        out.println("<label>kursId</label> <input type='text' name='kursId' value='"+kursId+"' readonly><br>");
        out.println("<label>modulNummer</label> <input type='number' name='modulNummer' value='"+modulNummer+"'><br>");
        out.println("<label>foreleserId</label> <input type='text' name='foreleserId' value='"+foreleserId+"' readonly><br>");
        out.println("</br> <label>oppgaveTekst</label> </br> <textarea cols='100' rows='10' name='oppgaveTekst'>"+oppgaveTekst+"</textarea><br>");
        out.println("<label>maks poeng</label> <input type='number' name='maxPoeng' value='"+maxPoeng+"'><br>");

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
