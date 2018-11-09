/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 *
 * @author Josef
 */
public class KalenderHendelse {
        
    Query query = new Query();
    ResultSet rs = null;
    
    protected int mottaker;
    protected String type;
    protected String refererer;
    protected String tekst;
    protected Timestamp startDato;
    protected Timestamp sluttDato;
    
    //Lagrer kalenderhendelse
    public void lagreHendelse(KalenderHendelse kalenderHendelse){
        query.update("INSERT INTO KalenderHendelse (mottakerId, hendelseType, hendelseRef, hendelseTekst, hendelseStartDato, hendelseSluttDato)"
                + " Values ("+this.mottaker+","+this.type+","+this.refererer+","+this.tekst+","+this.startDato+","+this.sluttDato+")");
    }
}