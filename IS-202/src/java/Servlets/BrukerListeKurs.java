/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Database.Query;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
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
 * @author Erlend Thorsen
 */
@WebServlet(name = "BrukerListeKurs", urlPatterns = {"/BrukerListeKurs"})
public class BrukerListeKurs extends HttpServlet {
    
    ResultSet rs = null;
    Query query = new Query();

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

            BrukerListe bl = new BrukerListe();
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title></title>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleBody.css'>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleNavbar.css'>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleLeftSidebar.css'>");
            out.println("</head>");
            out.println("<body>");
            
            HttpSession session = request.getSession();
            String kursId = request.getParameter("kursId");
            Navbar navbar = new Navbar();
            boolean isForeleser = (boolean)session.getAttribute("isForeleser");
            String redigerBrukere =  request.getParameter("redigerBrukere");

            

            out.println("<div class='mainContent'>");
            
            if(!redigerBrukere.equals("true")){
                //Skriver ut liste over studenter og forelesere
                String foreleser = ("SELECT A.fornavn, A.etterNavn, A.id from Foreleser A where  A.id in ( select B.foreleserId from ForeleserKurs B where B.kursId ='"+kursId+"')");
                String student = ("SELECT A.fornavn, A.etterNavn, A.id from Student A where  A.id in ( select B.studentId from TarKurs B where B.kursId ='"+kursId+"')");

                //Forelesere
                out.println("<h2>Studenter og forelesere som tar " +kursId+ "</h2>");
                out.println("<b>Forelesere:</b>");

                bl.skrivListe(foreleser, rs, query, out);

                //Studenter
                out.println("<b>Studenter:</b>");
                bl.skrivListe(student, rs, query, out);
                if(isForeleser){
                    out.println("<button type='submit' onclick=\"window.location.href='BrukerListeKurs?kursId="+kursId+"&redigerBrukere=true'\">Legg til/Fjern brukere</button>");
                }
            } else{
                out.println("<h2>Legg til eller fjern brukere fra "+kursId+"</h2>");
                out.println("<form action='OppdaterBrukereKurs?kursId="+kursId+"&redigerBrukere=true' method='POST'>");
                out.println("<div class='selectBrukere'");
                out.println("<label>Alle brukere</label>");
                out.println("<select name='brukereIkkeIKurs' size='15' multiple>");
                printBrukereIkkeIKurs(kursId,out);
                out.println("</select>");
                out.println("</div>");
                out.println("<div class='buttonStack'>");
                out.println("<input type='submit' name='leggTilBrukere' value='➜'>");
                out.println("<input type='submit' name='fjernBrukere' style='-webkit-transform: rotate(-180deg);' value='➜'>");
                out.println("</div>");
                out.println("<div class='selectBrukere'");
                out.println("<label>Brukere i Kurset</label>");
                out.println("<select name='brukereIKurs' size='15' multiple>");
                printBrukereIKurs(kursId,out);
                out.println("</select>");
                out.println("</div>");
                out.println("</form>");
            }
           

            out.println("</div>");
            //printer sidebar og navbar
            navbar.printLeftSidebar("Personer", kursId, out);
            try {
                navbar.printNavbar("Kurs",(String)session.getAttribute("id"),(boolean)session.getAttribute("isForeleser"), out);
            } catch (SQLException ex) {
                Logger.getLogger(Kurs.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    private void printBrukereIkkeIKurs(String kursId, PrintWriter out){
            //Printer Alle forelesere som ikke er i kurset til en <option>
            rs = query.query("SELECT id, forNavn, etterNavn FROM Foreleser WHERE id NOT IN(SELECT foreleserId FROM foreleserKurs where kursId='"+kursId+"')");
            out.println("<option class='optionLabel' value='label' disabled>Forelesere:</option>");
            printOption(out);
            //Printer Alle studenter som ikke er i kurset til <option>
            rs = query.query("SELECT id, forNavn, etterNavn FROM Student WHERE id NOT IN(SELECT studentId FROM TarKurs where kursId='"+kursId+"')");
            out.println("<option class='optionLabel' value='label' disabled>Studenter:</option>");
            printOption(out);
       
    }

    private void printBrukereIKurs(String kursId, PrintWriter out){
            //Printer Alle forelesere som er i kurset til en <option>
            rs = query.query("SELECT id, forNavn, etterNavn FROM Foreleser WHERE id IN(SELECT foreleserId FROM foreleserKurs where kursId='"+kursId+"')");
            out.println("<option class='optionLabel' value='label' disabled>Forelesere:</option>");
            printOption(out);

            //Printer Alle studenter som er i kurset til <option>
            rs = query.query("SELECT id, forNavn, etterNavn FROM Student WHERE id IN(SELECT studentId FROM TarKurs where kursId='"+kursId+"')");
            out.println("<option class='optionLabel' value='label' disabled>Studenter:</option>");
            printOption(out);        
    }
        
    private void printOption(PrintWriter out){
        //Skriver ut resultset til <option>
        try {
            String id = null;
            String fornavn = null;
            String etternavn = null;
            
            while(rs.next()){
                id = rs.getString(1);
                fornavn = rs.getString(2);
                etternavn = rs.getString(3);            
                out.println("<option value="+id+">"+fornavn+ " "+etternavn+"</option>");           
           }
        } catch (SQLException ex) {
            Logger.getLogger(BrukerListeKurs.class.getName()).log(Level.SEVERE, null, ex);
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
