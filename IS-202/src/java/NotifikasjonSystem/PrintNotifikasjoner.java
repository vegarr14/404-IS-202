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

    //Printer alle uleste notifikasjoner for den enkelte bruker
    public void printUleste(String id, String type, JspWriter JW, PrintWriter PW) throws IOException{
        
         ArrayList<String> notArray = new ArrayList<String>();
        rs = query.query("Select * from notifikasjoner WHERE mottakerId="+id+" AND notUlest=1 order by notOpprettet desc");
     
        try {
            notArray.add("<h2>Nye Notifikasjoner</h2>");
            notArray.add("<hr>");
            if(!rs.next()){
                notArray.add("<h4> Du har ingen nye notifikasjoner :(</h4>");
            }else{
                notArray.add("<ul>");
                rs.previous();
                while(rs.next()){
                    
                    String notId = rs.getString(1);
                    timestamp = rs.getString(7);
                    String notType = rs.getString(5);
                    int senderId = rs.getInt(3);
                    String notReferererId = rs.getString(6);
                    
                    melding = produserMelding(notType, senderId, notReferererId);
                    
                    notArray.add("<li>");
                    notArray.add("<div>");

                    notArray.add("<a href='NotifikasjonVideresender?id="+id+"&notId="+notId+"&notType="+notType+"&notRefId="+notReferererId+"'>"+melding+"</a>");
                    notArray.add("<p>Dette skjedde den <time>"+timestamp+"</time>.</p>");
                    notArray.add("</div>");
                    notArray.add("</li>");
                    notArray.add("<hr>");
                }
                notArray.add("</ul>");
            }
            
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
        
    }
    public void printLeste(String id, String type, JspWriter JW, PrintWriter PW) throws IOException{
        
         ArrayList<String> notArray = new ArrayList<String>();
        rs = query.query("Select * from notifikasjoner WHERE mottakerId="+id+" AND notUlest=0 order by notOpprettet desc");
        
        try {          
            if(!rs.next()){
                notArray.add("<h4> Du har ingen gamle notifikasjoner :(</h4>");
            }else{
                notArray.add("<h2>Gamle Notifikasjoner</h2>");
                notArray.add("<hr>");
                notArray.add("<ul>");
                rs.previous();
                while(rs.next()){
                    
                    String notId = rs.getString(1);
                    timestamp = rs.getString(7);
                    String notType = rs.getString(5);
                    int senderId = rs.getInt(3);
                    String notReferererId = rs.getString(6);
                    
                    melding = produserMelding(notType, senderId, notReferererId);
                    
                    notArray.add("<li>");
                    notArray.add("<div>");

                    notArray.add("<a href='NotifikasjonVideresender?id="+id+"&notId="+notId+"&notType="+notType+"&notRefId="+notReferererId+"'>"+melding+"</a>");
                    notArray.add("<p>Dette skjedde den <time>"+timestamp+"</time>.</p>");
                    notArray.add("</div>");
                    notArray.add("<hr>");
                    notArray.add("</li>");
                    
                }
                notArray.add("</ul>");
            }
            
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
        
    }
    
    //Produserer melding basert på type, sender og hvor den blir referert til
    private String produserMelding(String notType, int senderId, String notReferererId){
        
        ResultSet rs2 = null;
        String senderNavn;
        String kursId;
        String modulNummer;
        
        String s = "Error et eller annet";
        try {
            //notifikasjoner til modul
            if(notType.equals("nyModul") | notType.equals("oppdatertModul" ) | notType.equals("slettetModul")){

                //henter navn til sender
                senderNavn = getSenderName("foreleser", senderId);
                if(notType.equals("slettetModul")){
                    s = senderNavn + " har fjernet en modul. Vi vet desverre ikke hvilken siden den ble slettet #Irony";
                }else{
                    //Finner modulnummer og kursid som hører til notifikasjonen
                    rs2 = query.query("Select kursId, modulNummer from modul where modulId="+notReferererId+"");
                    rs2.next();
                    kursId = rs2.getString(1);
                    modulNummer = rs2.getString(2);

                    if(notType.equals("nyModul")){
                        s = senderNavn + " har opprettet en ny modul i " + kursId + "<br> Modul " + modulNummer;
                    }
                    else if(notType.equals("oppdatertModul")){
                        s = senderNavn + " har oppdatert modul " + modulNummer + " i " + kursId;
                    }
                }
            }
            //notifikasjoner til innlevering
            else if(notType.equals("nyInnlevering")){
                //henter navn til sender
                senderNavn = getSenderName("student", senderId);
                //Finner modulnummer og kursid som hører til notifikasjonen
                rs2 = query.query("Select kursId, modulNummer from modul where modulId in(select modulId from innlevering where innlevId="+notReferererId+")");
                rs2.next();
                kursId = rs2.getString(1);
                modulNummer = rs2.getString(2);
                
                s = senderNavn + " har levert modul " + modulNummer + " i " + kursId;
            }            
            //Notifikajsoner om fjenet fra eller lagt tilkurs
            else if(notType.equals("fjernetFraKurs") | notType.equals("lagtTilKurs")){
                //henter navn til sender
                senderNavn = getSenderName("foreleser", senderId);
                if(notType.equals("fjernetFraKurs")){
                    s = "Du har blitt fjernet fra " + notReferererId + " av " + senderNavn;
                }else{
                    s = "Du er blitt lagt til i kurset " + notReferererId + " av " + senderNavn;
                }
                
            }
        } catch (SQLException ex) {
                Logger.getLogger(PrintNotifikasjoner.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            return s;
    }
    // Henter fornavn og etternavn til sender
    private String getSenderName(String table, int senderId) throws SQLException{
        ResultSet rs2 = null;
        
        rs2 = query.query("Select forNavn, etterNavn from "+table+" where id="+senderId+"");
        rs2.next();
        String s = rs2.getString(1) + " " + rs2.getString(2);
        return s;
    }
}
