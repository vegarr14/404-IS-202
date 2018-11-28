/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NotifikasjonSystem.subclasses;

import Database.Query;
import NotifikasjonSystem.Notifikasjon;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Erlend Thorsen
 */
public class SlettetModulNotifikasjon extends Notifikasjon {
    
    //Setter variabler og lager notifikasjon til alle som skal ha det
    public void getAndSetSlettetModul(String kursId, String foreleserId, String modulId){
        
        this.type = "slettetModul";
        this.sender = Integer.parseInt(foreleserId);
        ResultSet rs = null;
        Query query = new Query();
        //Henter modulnummer for å kunne vise hvilken  modul som ble slettet
        rs = query.query("Select modulNummer, kursId from modul where modulId="+modulId+"");
        try{
            rs.next();
            this.refererer = rs.getString(1) + " i " + rs.getString(2);
        } catch (SQLException ex) {
            Logger.getLogger(OppdatertModulNotifikasjon.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.opprettet = getTimestamp();
        
        //query for alle brukere som skal ta få notifikasjonen
        rs = query.query("Select studentId from tarkurs WHERE kursId='"+kursId+"'");
        
        try {
            while(rs.next()){
                this.mottaker = rs.getInt(1);
                lagreNotifikasjon(this);
            }
            //sletter alle notifikasjoner som refererer til denne modulen
            rs = query.query("Select notId from Notifikasjoner Where notType in ('nyModul', 'oppdatertModul') AND notReferererId ='"+modulId+"'");
            while(rs.next()){
                slettNotifikasjon(rs.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(OppdatertModulNotifikasjon.class.getName()).log(Level.SEVERE, null, ex);
        }
        query.close();
    }
}
