/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database.KalenderHendelser.subclasses;

import Database.KalenderHendelse;
import Database.Query;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Josef
 */
public class InnleveringsfristHendelse extends KalenderHendelse {
    
    public void getAndSetHendelse(String modulId, Timestamp dato){
        
        this.type = "innlevfrist";
        this.refererer  = modulId;
        
        Calendar k = Calendar.getInstance();
        k.setTimeInMillis(dato.getTime());
        k.set(Calendar.HOUR_OF_DAY, 0);
        k.set(Calendar.MINUTE, 0);
        k.set(Calendar.SECOND, 0);
        dato.setTime(k.getTimeInMillis());
        this.startDato = (dato);
        
        k.set(Calendar.HOUR_OF_DAY, 23);
        k.set(Calendar.MINUTE, 59);
        dato.setTime(k.getTimeInMillis());
        this.sluttDato = (dato);
        
        String modulnr = null;
        String kursId = null;
        
        Query query = new Query();
        ResultSet rs = null;
        try {
        rs = query.query("Select modulNummer, kursId from modul where modulId="+modulId);
        
             rs.next();
             modulnr = rs.getString(1);
             kursId = rs.getString(2);
 
            this.tekst = "Innleveringsfrist for Modul " +modulnr + " i " + kursId;
        
            rs = query.query("select studentId from tarkurs where kursId ='"+kursId+"'");
            
            while(rs.next()){
                this.mottaker = rs.getInt(1);
                lagreHendelse(this);
            }
       
        } catch (SQLException ex) {
             Logger.getLogger(InnleveringsfristHendelse.class.getName()).log(Level.SEVERE, null, ex);
        } finally   {
            query.close();
        }
    }
}
