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
public class OppdatertModulNotifikasjon extends Notifikasjon {
    
    ResultSet rs = null;
    Query query = new Query();
    
    //Setter variabler og lager notifikasjon til alle som skal ha det
    public void getAndSetOppdatertModul(String kursId, String foreleserId, String modulId){
        
        this.type = "oppdatertModul";
        this.sender = Integer.parseInt(foreleserId);
        this.refererer = modulId;
        this.opprettet = getTimestamp();
        
        rs = query.query("Select studentId from tarkurs WHERE kursId='"+kursId+"'");
        
        try {
            while(rs.next()){
                this.mottaker = rs.getInt(1);
                lagreNotifikasjon(this);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OppdatertModulNotifikasjon.class.getName()).log(Level.SEVERE, null, ex);
        }
        query.close();
    }
}
