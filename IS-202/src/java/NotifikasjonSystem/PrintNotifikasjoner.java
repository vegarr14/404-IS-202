/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NotifikasjonSystem;

import Database.Query;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.jsp.JspWriter;

/**
 *
 * @author Erlend Thorsen
 */
public class PrintNotifikasjoner {
    
    ResultSet rs = null;
    Query query = new Query();
    
    private String melding;
    private String timestamp;
    private String link;

    //Printer alle uleste notifikasjoner for den enkelte bruker
    public void printUleste(String id, String type, String plassering, JspWriter JW, PrintWriter PW) throws IOException{
        
         ArrayList<String> notArray = new ArrayList<String>();
        rs = query.query("Select * from notifikasjoner WHERE mottakerId="+id+" AND notUlest=1 order by notOpprettet desc");
     
        try {
            notArray.add("<div class='notifikasjoner' id='"+plassering+"'>");
            notArray.add("<h2>Nye Notifikasjoner</h2>");
            notArray.add("<hr>");
            if(!rs.next()){
                notArray.add("<h4> Du har ingen nye notifikasjoner :(</h4>");
            }else{
                notArray.add("<ul>");
                rs.previous();
                while(rs.next()){
                    
                    timestamp = rs.getString(7);
                    String notType = rs.getString(5);
                    int senderId = rs.getInt(3);
                    int notReferererId = rs.getInt(6);
                    
                    melding = produserMelding(notType, senderId, notReferererId);
                    
                    notArray.add("<li>");
                    notArray.add("<div>");

                    notArray.add("<a href='NotifikasjonVideresender?id="+id+"&notType="+notType+"&notRefId="+notReferererId+"'>"+melding+"</a>");
                    notArray.add("<p>Dette skjedde den <time>"+timestamp+"</time>.</p>");
                    notArray.add("</div>");
                    notArray.add("</li>");
                    notArray.add("<hr>");
                }
                notArray.add("</ul>");
            }
            
            notArray.add("</div>");
        } catch (SQLException ex) {
            Logger.getLogger(PrintNotifikasjoner.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //velger type Writer og skriver ut navbar
            if(type == "JSP"){
                
                for(String linje: notArray) {
                    JW.println(linje);
                }
                
            }else{
              
                for(String linje: notArray) {
                    PW.println(linje);
                }
                
            }
        query.close();
        
    }
    //Produserer melding basert på type, sender og hvor den blir referert til
    private String produserMelding(String notType, int senderId, int notReferererId){
        
        ResultSet rs2 = null;
        String senderNavn;
        String kursId;
        String modulNummer;
        
        String s = "Error et eller annet";
        
        if(notType.equals("nyModul")){
            
            try {
                rs2 = query.query("Select forNavn, etterNavn from foreleser where id="+senderId+"");
                rs2.next();
                senderNavn = rs2.getString(1) + " " + rs2.getString(2);
                
                rs2 = query.query("Select kursId, modulNummer from modul where modulId="+notReferererId+"");
                rs2.next();
                kursId = rs2.getString(1);
                modulNummer = rs2.getString(2);
                
                s = senderNavn + " har opprettet en ny modul i " + kursId + "<br> Modul " + modulNummer;
              
                link = "Modul?kursId="+kursId+"&modulId="+notReferererId+"";
            } catch (SQLException ex) {
                Logger.getLogger(PrintNotifikasjoner.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
            return s;
    }    
}
