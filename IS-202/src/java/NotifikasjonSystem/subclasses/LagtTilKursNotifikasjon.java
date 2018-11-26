/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NotifikasjonSystem.subclasses;

import NotifikasjonSystem.Notifikasjon;

/**
 *
 * @author Erlend Thorsen
 */
public class LagtTilKursNotifikasjon extends Notifikasjon{
    
    //Setter variabler og lager notifikasjon til alle som skal ha det
    public void getAndSetLagtTilKurs(String kursId, String foreleserId, String[] studentId){
        
        this.type = "lagtTilKurs";
        this.sender = Integer.parseInt(foreleserId);
        this.refererer = kursId;
        this.opprettet = getTimestamp();
               
        for(String id : studentId){
            this.mottaker = Integer.parseInt(id);
            lagreNotifikasjon(this);
        }  
    }
} 

