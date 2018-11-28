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
public class InnleveringsFristNotifikasjon extends Notifikasjon {
        
     
    public void getAndSetInnlevFrist(String  modulId, int foreleserId){
        ResultSet rs = null;
    
        this.type = "24hInnlevFrist";
        this.refererer = modulId;
        this.opprettet = getTimestamp();
        this.sender = foreleserId;
        
        try{
            Query query = new Query();
            rs = query.query("Select id from student where id in (select studentId from TarKurs where kursId in ( select kursId from modul where modulId = "+modulId+")) AND id not in (select id from innlevering where modulId="+modulId+")");
            while(rs.next()){
                this.mottaker = rs.getInt(1);
                lagreNotifikasjon(this);
            }
            query.close();
        } catch (SQLException ex) {
            Logger.getLogger(InnleveringsFristNotifikasjon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
}
