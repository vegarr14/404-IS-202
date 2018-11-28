/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Klasser;

import Database.Query;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.jsp.JspWriter;

/**
 *
 * @author Arton
 */
public class Gjoremaal {
    
    public void printGjoremaal(String id, JspWriter out) throws IOException{
        ResultSet rs = null;
        Query query = new Query();
        
        String modulnummer = null;
        String kursId = null;
        String innleveringsfrist = null;
        String modulId = null;
        
        //hva vi henter av data fra databasen
        rs = query.query("select modulnummer, kursId, innleveringsfrist, modulId from modul where kursId in (select kursId from TarKurs where studentId = "+id+") and modulId not in (select modulId from innlevering where id = "+id+") order by innleveringsfrist desc");
        try { 
        //tittelen til lista
            out.println("<ul class='gjoremaalliste'>");
            while(rs.next()){
                modulnummer = rs.getString(1);
                kursId = rs.getString(2);
                innleveringsfrist = rs.getString(3);
                modulId = rs.getString(4);
                
                //lagrer alt i en databasen
                out.println("<li>");
                out.println("<div class='gjoremaal'>");
                out.println("<a href='Modul?kursId="+kursId+"&modulId="+modulId+"'>Modul "+modulnummer+" | " + kursId+"</a>");
                
                 
                if (innleveringsfrist != null){
                //Printer og fjerner sekunder fra inleveringsfrist
                out.println("<p>Innleverings frist er " + innleveringsfrist.substring(0, innleveringsfrist.length() -3) +"</p>");
                //Hvis det ikke er satt noe innleveringsfrist enda får du opp at frist ikke er satt
                } else if (innleveringsfrist == null) {
                    out.println("<p>Innleveringsfrist er ikke satt</p>");
                }
                out.println("<hr>");
                out.println("</div>");
                out.println("</li>");             
            }
            out.println("</ul>");
        } catch (SQLException ex) {
            Logger.getLogger(Gjoremaal.class.getName()).log(Level.SEVERE, null, ex);
            }
        query.close();
    }
}
