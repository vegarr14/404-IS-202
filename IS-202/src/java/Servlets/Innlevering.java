/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Database.Query;
import NotifikasjonSystem.subclasses.*;
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
    NyInnleveringNotifikasjon nyInnlevNot = new NyInnleveringNotifikasjon();
    InnleveringRettetNotifikasjon innlevRettet = new InnleveringRettetNotifikasjon();
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
            //Sjekker om det er foreleser eller student som er innlogget
            HttpSession session = request.getSession();

            ResultSet rs = null;
            Query query = new Query();
            int innlevId = 0;
            System.out.println(request.getParameter("submit"));
            System.out.println(request.getParameter("innlevPoeng"));
            if (request.getParameter("submit") != null) {
                System.out.println("test");
                query.update("update Innlevering set innlevPoeng='" + request.getParameter("innlevPoeng") + "' where innlevId='" + request.getParameter("innlevId") + "'");
                int foreleserId = Integer.parseInt((String)session.getAttribute("id"));
                String sInnlevId = request.getParameter("innlevId");
                //lager notifikasjon til om at innlevering er rettet
                innlevRettet.getAndSetInnRettNot(sInnlevId, foreleserId);
            }
            if (request.getParameter("button") != null) {
                rs = query.query("SELECT Modul.levereSomGruppe FROM Modul WHERE Modul.modulId = " + request.getParameter("modulId") + "");
                int levereSomGruppe = 0;
                try {
                    rs.next();
                    levereSomGruppe = rs.getInt(1);
                    rs = null;
                } catch (SQLException ignore) {
                }
                if (levereSomGruppe == (1)) {
                    innlevId = innleveringGruppe(request, response, session, out);
                } else {
                    innlevId = innleveringIndividuell(request, response, session, out);
                }
            } else {
                innlevId = Integer.parseInt(request.getParameter("innlevId"));
            }
            printInnlevering(request, response, session, out, innlevId);
            out.println("</div>");
        }
    }
    
    //Viser en innlevering og gir mulighet til å laste ned fil som hører til innlevering
    public void printInnlevering(HttpServletRequest request, HttpServletResponse response, HttpSession session, PrintWriter out, int innlevId)
            throws ServletException, IOException {
         {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Innlevering</title>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleNavbar.css'>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleBody.css'>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleLeftSidebar.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class=mainContent>");
            out.println("<h1>Innlevering</h1>");
            try {
                Query query = new Query();
                ResultSet rs = query.query("select fileName, innlevId, innlevKommentar, innlevPoeng, maxPoeng from Innlevering A join Modul B where A.innlevId = '"+innlevId+"' and A.modulId = B.modulId");
                rs.next();
                out.println(rs.getString(3)+"<br>");
                if (rs.getString(1)!=null) {
                    out.println("<a href='Download?innlevId="+rs.getString(2)+"'>" +rs.getString(1)+ "</a>");
                }
                poeng(rs,out,(boolean)session.getAttribute("isForeleser"));
                
                

                
                Navbar navbar = new Navbar();
                navbar.printLeftSidebar("Moduler", request.getParameter("kursId"), out);
                navbar.printNavbar("Kurs", (String)session.getAttribute("id"),(boolean)session.getAttribute("isForeleser"), out);
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.println("</body>");
            out.println("</html>");
        }
    }
    //skriver ut poeng
    public void poeng(ResultSet rs, PrintWriter out, boolean isForeleser) {
        try {
            if(isForeleser==true) {
                out.println("<br><br><form action='Innlevering' method='POST'>");
                out.println("<input type='hidden' name='innlevId' value='"+rs.getInt(2)+"'>");
                out.println("<input type='number' max='"+rs.getInt(5)+"' name='innlevPoeng' value='"+rs.getInt(4)+"'> av "+rs.getInt(5)+" mulige poeng");
                out.println("<input type=submit name=submit value='oppdater'></form>");
                //out.println("<button type='submit' name='submit' onclick=\"window.location.href='Innlevering?innlevId="+rs.getInt(2)+"'\"> oppdater </button>");
            } else if(rs.getString(4)!=null) {
                out.println(rs.getString(4)+" av "+rs.getString(5)+" mulige poeng");
            } else {
                out.println("<br>Ikke rettet enda");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    //Legger inn innlevering i database
    public int innleveringIndividuell(HttpServletRequest request, HttpServletResponse response, HttpSession session, PrintWriter out)
            throws ServletException, IOException {
        Query query = new Query();
        ResultSet rs = null;
        boolean file = false;
        int innlevId = 0;
        String id = (String) session.getAttribute("id");
        String modulId = request.getParameter("modulId");
        try {
            for (Part part : request.getParts()) {
                String name = part.getSubmittedFileName();
                if (name != null && name.length() > 0) {
                    // File data
                    System.out.println("OMEGALUL OMEGALUL");
                    InputStream is = part.getInputStream();
                    String insert = "insert into Innlevering(fileName, fileData, modulId, id, innlevKommentar) values('" + name + "',?," + modulId + ","
                            + id + ",'" + request.getParameter("kommentar") + "')";
                    query.insertFile(insert, is);
                    file = true;
                    
                }
            }
            if (file == false) {
                System.out.println("insert into Innlevering(modulId, id, innlevKommentar) values"
                        + "("+request.getParameter("modulId")+","+(String)session.getAttribute("id")+",'"+request.getParameter("kommentar")+"')");
                query.update("insert into Innlevering(modulId, id, innlevKommentar) values"
                        + "("+request.getParameter("modulId")+","+(String)session.getAttribute("id")+",'"+request.getParameter("kommentar")+"')");
            }
            rs = query.query("select max(innlevId) from Innlevering");
            rs.next();
            innlevId = rs.getInt(1);
            
            //lager notifikasjon
            lagNotifikasjon(id,modulId);
            
        } catch (SQLException ignore) {}
        query.close();
        return innlevId;
    }
    
    public int innleveringGruppe(HttpServletRequest request, HttpServletResponse response, HttpSession session, PrintWriter out)
            throws ServletException, IOException {
        Query query = new Query();
        ResultSet rs = null;
        boolean file = false;
        int innlevId = 0;
        String finngruppeNavn
                = "select gruppe.gruppeId from gruppe\n"
                + "inner join Gruppetilbruker ON gruppe.gruppeId = Gruppetilbruker.gruppeId\n"
                + "inner join Student ON Gruppetilbruker.id = Student.id\n"
                + "inner join gruppetilkurs ON gruppe.gruppeId = gruppetilkurs.gruppeId\n"
                + "where student.id = " + String.valueOf(session.getAttribute("id")) + " and gruppetilkurs.kursId = '" + (request.getParameter("kursId")) + "'";

        rs = query.query(finngruppeNavn);
        int gruppeId = 0;
        try {
            if (rs.next()) {

                gruppeId = rs.getInt(1);
                rs = null;
            }
            String id = (String) session.getAttribute("id");
            String modulId = request.getParameter("modulId");
            
            for (Part part : request.getParts()) {
                String name = part.getSubmittedFileName();
                if (name != null && name.length() > 0) {
                    // File data
                    out.println(part.getSubmittedFileName() + "</br>");
                    out.println(part.getSize() + "</br>");
                    InputStream is = part.getInputStream();
                    String insert = "insert into Innlevering(fileName, fileData, modulId, id, innlevKommentar, gruppeId) values('" + name + "',?," + modulId + ","
                            + id + ",'" + request.getParameter("kommentar") + "','" + gruppeId + "')";
                    query.insertFile(insert, is);
                    file = true;
                }
            }
            if (file == false) {
                query.update("insert into Innlevering(modulId, id, innlevKommentar, gruppeId) values(" + request.getParameter("modulId") + ","
                            + (String) session.getAttribute("id") + ",'" + request.getParameter("kommentar") + "','" + gruppeId + "')");
            }
            rs = query.query("select max(innlevId) from Innlevering");
            rs.next();
            innlevId = rs.getInt(1);
            
            //lager notifikasjon
            lagNotifikasjon(id,modulId);
        } catch (SQLException ignore) {
        }
        query.close();
        return innlevId;
    }
    
    private void lagNotifikasjon(String id, String modulId){
            //Spør etter kursid og innlevid til notifikasjon
            ResultSet rs = null;
            Query query = new Query();
            
            rs = query.query("Select kursId, innlevId from modul join innlevering on modul.modulId=innlevering.modulId where innlevering.id="+id+" and innlevering.modulId="+modulId+"");
            try {
                if(rs.next()){
                    String kursId = rs.getString(1);
                    String innlevIdS = rs.getString(2);
                    //Lager notifikasjoner for alle forelesere i kurset
                    nyInnlevNot.getAndSetnyInnlevering(kursId, id, innlevIdS);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Innlevering.class.getName()).log(Level.SEVERE, null, ex);
            }
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
