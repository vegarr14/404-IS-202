/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Database.*;
import Database.KalenderHendelser.subclasses.InnleveringsfristHendelse;
import NotifikasjonSystem.Notifikasjon;
import NotifikasjonSystem.subclasses.*;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

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
        request.setCharacterEncoding("UTF8");

        HttpSession session = request.getSession();
        try (PrintWriter out = response.getWriter()) {

            /*Lage nytt Query-objekt, resultset ( = null*/
            Query query = new Query();
            ResultSet rs = null;

            InnleveringsfristHendelse innlevHendelse = new InnleveringsfristHendelse();

            //Lager nytt notifikasjonobjekt
            NyModulNotifikasjon nyModNot = new NyModulNotifikasjon();
            OppdatertModulNotifikasjon oppModNot = new OppdatertModulNotifikasjon();
            SlettetModulNotifikasjon slettModNot = new SlettetModulNotifikasjon();

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Moduler</title>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleNavbar.css'>");

            out.println("<link rel='stylesheet' type='text/css' href='style/styleLeftSidebar.css'>");
            out.println("<link rel='stylesheet' type='text/css' href='style/moduloversikt.css'>");
            out.println("</head>");
            out.println("<body>");
            String kursId = request.getParameter("kursId");

            out.println("<div class='main-content'>");
            out.println("<h1> Moduler i " + kursId + "</h1>");

            if (request.getParameter("button") != null) {
                //sjekker om en knapp med name button er trykket på for å åpne siden
                String frist = request.getParameter("innleveringsFrist");
                String modulId = request.getParameter("modulId");
                String modulNummer = request.getParameter("modulNummer");
                String foreleserId = request.getParameter("foreleserId");
                String oppgaveTekst = request.getParameter("oppgaveTekst");
                String maxPoeng = request.getParameter("maxPoeng");
                String type1 = request.getParameter("oppgaveType");
                String type2 = "";
                if (type1 != null && !type1.isEmpty()) {
                    type2 = "1";
                } else {
                    type2 = "0";
                }
                if (request.getParameter("button").equals("legg til")) {
                    //Kjører hvis det skal legges til ny modul
                    Timestamp timestamp = getTimestamp(frist);
                    String a = "";
                    String b = "";
                    if (timestamp != null) {
                        a = ", innleveringsFrist";
                        b = ",'" + timestamp + "'";
                    }
                    query.update("INSERT into Modul (kursId, foreleserId, modulNummer, oppgaveTekst, levereSomGruppe, maxPoeng" + a + ") values('" + kursId + "','" + foreleserId + "','" + modulNummer + "','" + oppgaveTekst + "','" + type2 + "','" + maxPoeng + "'" + b + ")");
                    rs = query.query("Select modulId FROM modul WHERE kursId='"+kursId+"' AND foreleserId="+foreleserId+" AND modulNummer="+modulNummer+"");
                        try {
                            rs.next();
                            modulId = rs.getString(1);
                        } catch (SQLException ex) {
                            Logger.getLogger(ModulListe.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        if(timestamp !=null){
                            innlevHendelse.getAndSetHendelse(modulId, timestamp);
                        }
                        //Lager notifikasjoner for alle studenter i kurset
                        nyModNot.getAndSetNyModul(kursId, foreleserId, modulId);

                    } else if (request.getParameter("button").equals("oppdater modul")) {
                        //kjører hvis en modul skal oppdateres
                        Timestamp timestamp = getTimestamp(frist);
                        query.update("UPDATE Modul set kursId ='"+kursId+"',foreleserId='"+foreleserId+"',modulNummer ='"+modulNummer+"', oppgaveTekst ='"+oppgaveTekst+"', maxPoeng ='"+maxPoeng+"', innleveringsFrist ="+timestamp+" where modulId ='"+modulId+"'");
                        
                        //Lager notifikasjoner for alle studenter i kurset
                        oppModNot.getAndSetOppdatertModul(kursId, foreleserId, modulId);

                    } else if (request.getParameter("button").equals("slett modul")) {
                        //kjører hvis en modul skal slettes
                        //Lager notifikasjoner for slettet modul. Sletter også alle notifikasjoner som refererer til denne modulen
                        slettModNot.getAndSetSlettetModul(kursId, foreleserId, modulId);

                        query.update("DELETE from Modul where modulId = "+request.getParameter("modulId"));

                    }

                }
            

            try {
                //Henter moduler fra Modul-tablet i databasen
                rs = query.query("select modulId, modulNummer from Modul where Modul.kursId='" + kursId + "' order by modulNummer ");
                /*rs.last for å gå til siste raden i resultsettet, legger inn størrelsen på arrayet modulArray utifra 
                    hvilken rad det siste resultatet er på.*/
                rs.last();
                int[] modulArray = null;
                modulArray = new int[rs.getRow()];
                rs.beforeFirst(); //setter den tilbake til original posisjon

                int antModuler = 0;

                out.println("<table class='modulOversikt'>");
                out.println("<tr>");
                out.println("<th id='nostyle'></th>"); //en tom table header, den som er over student nr 1 og på venstre av modul nr 1

                //Imens resultsettet fra Query har den neste:
                //Print en link til modulen, koden hvis den blir trykket på er lengre ned i koden.
                //Legger også inn modulIden i modulArray og sjekker antall moduler som blir lagt inn til senere bruk.
                while (rs.next()) {
                    out.println("<th> <a href ='Modul?kursId=" + kursId + "&modulId=" + rs.getString(1) + "'> Modul " + rs.getString(2) + "</a></th>");
                    modulArray[antModuler] = rs.getInt(1);
                    antModuler++;
                }
                out.println("</tr>");
                /*rs henter innleveringene til studentene for moduler fra kurset brukeren er inne på (lagret i session).
                    Altså alle innleveringer for modul 1, 2, 3 og 4 hvis de er i feks IS-202 og innleveringene med studentene som hører til innleveringene og kurset.*/
                if ((boolean) session.getAttribute("isForeleser")) {
                    rs = query.query("select id from student join tarKurs on student.id = tarKurs.studentId and tarKurs.kursId = '" + kursId + "' order by etterNavn");
                } else {
                    rs = query.query("select id from student join tarKurs on student.id = tarKurs.studentId and tarKurs.kursId = '" + kursId + "' and student.id = '" + (String) session.getAttribute("id") + "'order by etterNavn");
                }
                rs.last();
                int[] studentArray;
                studentArray = new int[rs.getRow()];
                rs.beforeFirst();
                int antStudenter = 0;
                while (rs.next()) { //lager Studenter og Innleveringer (objekter).

                    studentArray[antStudenter] = rs.getInt(1);
                    antStudenter++;

                }
                if ((boolean) session.getAttribute("isForeleser")) {

                    rs = query.query("select student.id, forNavn, etterNavn, innlevPoeng, innlevering.modulId, innlevId from Student join Innlevering join Modul where Student.id = innlevering.id and '" + kursId + "'=modul.kursId and modul.modulId = innlevering.modulId order by etterNavn, modulNummer");
                } else {
                    rs = query.query("select student.id, forNavn, etterNavn, innlevPoeng, innlevering.modulId, innlevId from Student join Innlevering join Modul where Student.id = innlevering.id and modul.kursId='" + kursId + "' and modul.modulId = innlevering.modulId and student.id = '" + (String) session.getAttribute("id") + "' order by etterNavn, modulNummer");

                }
                int teller1 = 0;

                rs.beforeFirst();
                int antRader = 0;
                int antKolonner = 0;
                if (rs.next()) {
                    while (antRader < antStudenter) {
                        out.println("<tr>");
                        if (rs.isAfterLast() == false) {

                            out.println("<th class='rad'>" + rs.getString(3) + ", " + rs.getString(2) + "</th>");

                            while (antKolonner < antModuler) {
                                if (rs.isAfterLast() == true) {
                                    out.println("<td></td>");
                                } else if (rs.getInt(5) == modulArray[antKolonner] && rs.getInt(1) == studentArray[antRader]) {

                                    out.println("<td class='rad'> <a href='Innlevering?kursId="+kursId+"&innlevId="+rs.getString(6)+"'> Poeng: " + rs.getInt(4) + "</a></td>");

                                    rs.next();
                                } else {
                                    out.println("<td></td>");
                                }
                                antKolonner++;
                            }
                        }
                        out.println("</tr>");
                        antRader++;
                        antKolonner = 0;
                    }

                } else if (!(boolean) session.getAttribute("isForeleser")) {
                    rs = query.query("select forNavn, etterNavn from Student where id = '" + (String) session.getAttribute("id") + "'");
                    out.println("<tr>");
                    boolean skrevetStudent = false;
                    if (rs.last() == true && skrevetStudent == false) {
                        out.println("<th>" + rs.getString(2) + ", " + rs.getString(1) + "</th>");
                        skrevetStudent = true;
                    }
                    while (antRader < antStudenter) {
                        while (antKolonner < antModuler) {
                            out.println("<td></td>");
                            antKolonner++;
                        }
                        out.println("</tr>");

                        antRader++;
                        antKolonner = 0;
                    }
                }
                out.println("</table>");
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);

            }

            if ((boolean) session.getAttribute("isForeleser")) {
                out.println("<form name='Modul' action='Modul?kursId=" + kursId + "' method='post'>");
                out.println("<button type='submit'>Legg Til Modul</button>");
                out.println("</form>");
            }

            out.println("<form name='Kurs' action='Kurs?kursId=" + kursId + "' method='post'>");
            out.println("<button type='submit'>Tilbake</button>");
            out.println("</form>");
            out.println("</div>");

            try {
                Navbar navbar = new Navbar();
                navbar.printLeftSidebar("Moduler", request.getParameter("kursId"), out);

                navbar.printNavbar("Kurs", (String) session.getAttribute("id"), (boolean) session.getAttribute("isForeleser"), out);
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }

            out.println("</body>");
            out.println("</html>");
            query.close();
        }

    }



    private Timestamp getTimestamp(String s) {
        try {
            String frist = s.replace("T", " ");
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date date = simpleDate.parse(frist);


            Timestamp timestamp = new Timestamp(date.getTime());
            return timestamp;
        } catch (ParseException ignore) {
            return null;
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
