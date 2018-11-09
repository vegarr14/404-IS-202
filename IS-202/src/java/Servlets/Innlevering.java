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
        try (PrintWriter out = response.getWriter()) {
            //Sjekker om det er foreleser eller student som er innlogget
            HttpSession session = request.getSession();

            ResultSet rs = null;
            Query query = new Query();
            int innlevId = 0;
            if (request.getParameter("submit") != null) {
                query.update("update Innlevering set innlevPoeng='" + request.getParameter("innlevPoeng") + "' where innlevId='" + request.getParameter("innlevId") + "'");
            }
            if (request.getParameter("submitKommentar") != null) {
                query.update("insert into Kommentarer(id, innlevId, komKommentar) values('"+(String)session.getAttribute("id")+"','"+request.getParameter("innlevId")+"','"+request.getParameter("kommentar")+"')");
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
                
                
                kommentarer(query, innlevId, out);
                
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
                out.println("<input type='submit' name='submit' value='oppdater'></form>");
            } else if(rs.getString(4)!=null) {
                out.println(rs.getString(4)+" av "+rs.getString(5)+" mulige poeng");
            } else {
                out.println("<br>Ikke rettet enda");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public  void kommentarer(Query query, int innlevId, PrintWriter out) {
        out.println("Kommentarer: <br><br>");
        ResultSet rs = query.query("select komId, forNavn, etterNavn, komKommentar from Student A join Kommentarer B where A.id = B.id and B.innlevId = "+innlevId+" union "
                + "select komId, forNavn, etterNavn, komKommentar from Foreleser A join Kommentarer B where A.id = B.id and B.innlevId = "+innlevId
                        + " order by komId");
        try {
            while (rs.next()) {
                out.println(rs.getString(4)+"<br><br>");
            }
            out.println("<form action='Innlevering' method'post'>"
                    + "ny kommentar: <br>"
                    + "<input type='hidden' name='innlevId' value='"+innlevId+"'>"
                    + "<input type='text' name=kommentar>"
                    + "<input type='submit' name='submitKommentar' value='oppdater'> </form>");
        } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
}
    
    //Legger inn innlevering i database
    public int innleveringIndividuell(HttpServletRequest request, HttpServletResponse response, HttpSession session, PrintWriter out)
            throws ServletException, IOException {
        Query query = new Query();
        ResultSet rs = null;
        int innlevId = 0;
        rs = query.query("select innlevId from Innlevering where modulId = " + request.getParameter("modulId") + " and id = " + (String) session.getAttribute("id"));
        try {
            if (rs.next() == true) {
                query.update("delete * from Innlevering where innlevId = '" + rs.getInt(1) + "'");
            }
            String s = "insert into Innlevering(fileName, fileData, modulId, id, innlevKommentar) values(?,?," + request.getParameter("modulId") + ","
                    + (String) session.getAttribute("id") + ",'" + request.getParameter("kommentar") + "')";
            String u = "insert into Innlevering(modulId, id, innlevKommentar) values"
                    + "(" + request.getParameter("modulId") + "," + (String) session.getAttribute("id") + ",'" + request.getParameter("kommentar") + "')";
            innlevId = partsToInput(query, request, response, s, u);
        } catch (SQLException ignore) {
        }
        query.close();
        return innlevId;
    }
    
    public int innleveringGruppe(HttpServletRequest request, HttpServletResponse response, HttpSession session, PrintWriter out)
            throws ServletException, IOException {
        Query query = new Query();
        ResultSet rs = null;
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
            rs = query.query("select innlevId from Innlevering where gruppeId = '"+gruppeId+"' and modulId = '"+request.getParameter("modulId")+"'");
            if (rs.next()) {
                innlevId = rs.getInt(1);
                query.update("delete from Innlevering where innlevId='"+innlevId+"'");
            }
            String s = "insert into Innlevering(fileName, fileData, modulId, id, innlevKommentar, gruppeId) values(?,?," + request.getParameter("modulId") + ","
                    + (String) session.getAttribute("id") + ",'" + request.getParameter("kommentar") + "','" + gruppeId + "')";

            String u = "insert into Innlevering(modulId, id, innlevKommentar, gruppeId) values(" + request.getParameter("modulId") + ","
                    + (String) session.getAttribute("id") + ",'" + request.getParameter("kommentar") + "','" + gruppeId + "')";

            innlevId = partsToInput(query, request, response, s, u);
        } catch (SQLException ignore) {
        }
        query.close();
        return innlevId;
    }

    public int partsToInput (Query query, HttpServletRequest request, HttpServletResponse response, String s, String u) 
            throws ServletException, IOException, SQLException {
        ResultSet rs;
        boolean file = false;
        for (Part part : request.getParts()) {
            String name = part.getSubmittedFileName();
            if (name != null && name.length() > 0) {
                // File data
                InputStream is = part.getInputStream();
                query.insertFile(s, is, name);
                file = true;
            }
        }
        if (file == false) {
            query.update(u);
        }
        rs = query.query("select max(innlevId) from Innlevering");
        rs.next();
        return rs.getInt(1);
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
