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
public class InnleveringRettetNotifikasjon extends Notifikasjon{
    
    public void getAndSetInnRettNot(String innlevId, int foreleserId){
        ResultSet rs = null;
        Query query = new Query();
        
        this.type = "InnlevRettet";
        this.sender = foreleserId;
        this.refererer = innlevId;
        this.opprettet = getTimestamp();
        
        rs = query.query("SELECT id,gruppeId from innlevering where innlevId ="+innlevId);
        try{
                rs.next();
                //sjekker om det er en gruppeinnlevering
                if(rs.getInt(2) != 0){
                    String gruppeId = rs.getString(2);
                    rs = query.query("SELECT id from gruppetilbruker where gruppeId ="+gruppeId);
                    while(rs.next()){
                        this.mottaker = rs.getInt(1);
                        lagreNotifikasjon(this);
                    }
                }else{
                    this.mottaker = rs.getInt(1);
                    lagreNotifikasjon(this);
                }
            
        } catch (SQLException ex) {
            Logger.getLogger(InnleveringRettetNotifikasjon.class.getName()).log(Level.SEVERE, null, ex);
        }
        query.close();
    }
}
