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

/**
 *
 * @author Sondre
 */
@WebServlet(name = "LeggTilModul", urlPatterns = {"/LeggTilModul"})
public class LeggTilModul extends HttpServlet {

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
            
            /*Query query = new Query();
            ResultSet rs = null;
            PreparedStatement modulListe;*/
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LeggTilModul</title>");
            out.println("<link rel='stylesheet' type='text/css' href='style/modulListe.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LeggTilModul at " + request.getContextPath() + "</h1>");
                /*Velger alt fra modulListe-table fra MySQL og skriverModulliste. Se Query for mer.*/
                /*out.println("<table name=modulListe>");
                    query.skrivModulliste("SELECT * FROM modulListe", out);
                out.println("</table>");*/
                
            Query query = new Query();
            ResultSet rs = null;
            String id = request.getParameter("id");
            String modulNavn = "";
            String modulNummer = "";
            
            if(id!= null) {
                /* Hvis id parameteren inneholder noe (ikke lik null) har det blitt trykket på en 
                 * bruker i BrukerListe slik at informasjon om brukeren kommer opp i feltene
                 * + valg mellom oppdater bruker og slett bruker
                 */
                rs = query.query("select * from modulListe where id = "+id+" union select * from student where id = "+id);
                rs.next();
                modulNavn = rs.getString(2);
                modulNummer = rs.getString(3);
                
                out.println("Brukerid <input type='text' name='id' value='"+id+"' readonly><br>");
                printFelter(modulNavn, modulNummer,out);
                out.println("<input type='submit' name='button' value='oppdater bruker'>");
                out.println("<input type='submit' name='button' value='slett bruker'>");
            } else {
                //Hvis det er trykket på legg til bruker knappen skal tomme felter + radio knapper vises
                printFelter(modulNavn,modulNummer,out);
                out.println("<input type='radio' name='brukertype' value='student' checked> Student<br>");
                out.println("<input type='radio' name='brukertype' value='foreleser'> Foreleser<br>");
                out.println("<input type='submit' name='button' value='legg til'>");
            }
            
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        } catch (SQLException ex){
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
            /*    
            out.println("<form name='LeggTilModul' action='LeggTilModul'>");
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
    public void printFelter (String modulNavn, String modulNummer, PrintWriter out) {
        out.println("Fornavn <input type='text' name='Fornavn' value='"+modulNavn+"'><br>");
        out.println("Etternavn <input type='text' name='modulNummer' value='"+modulNummer+"'><br>");
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
