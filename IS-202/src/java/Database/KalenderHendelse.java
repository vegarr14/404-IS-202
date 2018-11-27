/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        query.update("INSERT INTO KalenderHendelse (mottakerId, hendelseType, hendelseRef, hendelseTekst, hendelseStartDato, hendelseSluttDato) Values ("+this.mottaker+",'"+this.type+"','"+this.refererer+"','"+this.tekst+"','"+this.startDato+"','"+this.sluttDato+"')");
    }
    
    public String[][] getArray(String id){
        System.out.println("metodeStart");
        String [][] kalenderArray = null;
        try {
            rs = query.query("Select * from KalenderHendelse where mottakerId = "+ id);
            rs.last();
            int antallRader = rs.getRow();
            
            kalenderArray = new String[antallRader][];
            
            rs.beforeFirst();
            int i = 0;
            int k = 0;
            System.out.println(i + " " + k);
            while(rs.next()){
                System.out.println(rs.getString(5));
                System.out.println(rs.getString(6));
                kalenderArray[i] = new String[] {rs.getString(5), rs.getString(6), rs.getString(7)};
                
                i++;
                
            } 
        } catch (SQLException ex) {
            Logger.getLogger(KalenderHendelse.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("før return");
        return kalenderArray;
    }
}