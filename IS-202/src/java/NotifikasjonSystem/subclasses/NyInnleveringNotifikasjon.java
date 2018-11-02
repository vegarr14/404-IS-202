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
public class NyInnleveringNotifikasjon extends Notifikasjon {
    
    ResultSet rs = null;
    Query query = new Query();
    
    //Setter variabler og lager notifikasjon til alle som skal ha det
    public void getAndSetnyInnlevering(String kursId, String studentId, String innlevId){
        
        this.type = "nyInnlevering";
        this.sender = Integer.parseInt(studentId);
        this.refererer = Integer.parseInt(innlevId);
        this.opprettet = getTimestamp();
        
        rs = query.query("Select foreleserId from foreleserKurs where kursId='"+kursId+"'");
        
        try {
            while(rs.next()){
                this.mottaker = rs.getInt(1);
                lagreNotifikasjon(this);
            }
        } catch (SQLException ex) {
            Logger.getLogger(NyInnleveringNotifikasjon.class.getName()).log(Level.SEVERE, null, ex);
        }
        query.close();
    }
}
