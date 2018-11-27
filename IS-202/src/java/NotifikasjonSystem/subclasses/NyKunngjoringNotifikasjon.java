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
public class NyKunngjoringNotifikasjon extends Notifikasjon {
    
    //Lager ny kunngjøring 
    public void getAndSetNyKungjoring(String kursId, int foreleserId){
        ResultSet rs = null;
        Query query = new Query();
 
        
        this.sender = foreleserId;
        this.refererer = kursId;
        this.type = "nyKunngjoring";
        this.opprettet = getTimestamp();
        
        rs = query.query("SELECT studentId from TarKurs WHERE kursId='"+kursId+"'");
        try{
            while(rs.next()){
                this.mottaker = rs.getInt(1);
                lagreNotifikasjon(this);
            }
        } catch (SQLException ex) {
            Logger.getLogger(NyKunngjoringNotifikasjon.class.getName()).log(Level.SEVERE, null, ex);
        }
        query.close();
    }
}