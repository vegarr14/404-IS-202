/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NotifikasjonSystem;

import Database.Query;
import java.sql.Timestamp;

/**
 *
 * @author Erlend Thorsen
 */
public class Notifikasjon {
    
    Query query = new Query();
    
    //Felles variabler
    protected int mottaker;
    protected int sender;
    protected boolean ulest;
    protected String type;
    protected String refererer;
    protected Timestamp opprettet;
    
    //Lagrer notifikasjonen
    public void lagreNotifikasjon(Notifikasjon notifikasjon){
        query.update("INSERT INTO Notifikasjoner(mottakerId, senderId, notType, notReferererId, notOpprettet) VALUES ("+this.mottaker+","+this.sender+",'"+this.type+"','"+this.refererer+"','"+this.opprettet+"')");
    }
    
    public void slettNotifikasjon(int notId){
        query.update("DELETE FROM Notifikasjoner WHERE notId="+notId+"");
    }
    
    public Timestamp getTimestamp(){
        //henter dato og tid
        long millis = System.currentTimeMillis();
        Timestamp ts = new Timestamp(millis);
        return ts;
    }
}
