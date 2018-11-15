/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NotifikasjonSystem.subclasses;

import Database.Query;
import NotifikasjonSystem.Notifikasjon;
import java.sql.ResultSet;

/**
 *
 * @author Erlend Thorsen
 */
public class InnleveringsFristNotifikasjon extends Notifikasjon {
        
    ResultSet rs = null;
    Query query = new Query();
    
    public void getAndSetInnlevFrist(String  modulId, String foreleserId){
        this.type = "24hInnlevFrist";
        this.refererer = modulId;
        this.opprettet = getTimestamp();
        
    }   
}
