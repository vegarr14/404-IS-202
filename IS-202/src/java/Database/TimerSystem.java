/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import NotifikasjonSystem.subclasses.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/**
 *
 * @author Erlend Thorsen
 */
public class TimerSystem implements javax.servlet.ServletContextListener {
    
    public void start() {
        final TimerSystem ts = new TimerSystem();
        System.out.println("Pling plong ding dong timersystem startet");
        InnleveringsFristNotifikasjon innFristNot = new InnleveringsFristNotifikasjon();  
        Timer timer = new Timer();
        
        ArrayList alleredeSendtNot = new ArrayList();
        
        Query query = new Query();
        ResultSet rs = null;
        //legger allerede sendte notifikasjoner i array listen.
        rs = query.query("SELECT Distinct notReferererId from notifikasjoner where notType='24hInnlevFrist'");
        try {
            while(rs.next()){     
                alleredeSendtNot.add(rs.getString(1));
            }            
        } catch (SQLException ex) {
            Logger.getLogger(TimerSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        query.close();
        //kjører hver time
        TimerTask hourlyTask = new TimerTask () {            
        @Override
        public void run () {
            Query query = new Query();
            ResultSet rs = null;
            
            System.out.println("Starter timedlige oppgaver...");
            // sjekker innleveringsfrist
            System.out.println("Sjekker om noen moduler har innleveringsfrist innen 24 timer");
            rs = query.query("Select modulId, foreleserId from modul where(TIMESTAMPDIFF(Hour, CURRENT_TIMESTAMP(), innleveringsFrist) <= 24)");
            try {
                if(rs.next()){
                    rs.beforeFirst();
                    while(rs.next()){
                        String modulId = rs.getString(1);
                        int foreleserId = rs.getInt(2);
                        //sjekker om det allerede er sendt notifikasjon på denne modulen
                        if(alleredeSendtNot.contains(modulId)){
                            System.out.println("Notifikasjon allerede sendt for modulen med id " + modulId);
                        }else{
                            System.out.println("Modulen med id "+ modulId +" har innlevering innen 24 timer. Oppretter notifikasjon.");
                            innFristNot.getAndSetInnlevFrist(modulId, foreleserId);
                            alleredeSendtNot.add(modulId);
                        }
                    }
                }else{
                    System.out.println("Ingen moduler har innleveringsfrist innen 24 timer");
                }
            } catch (SQLException ex) {
                Logger.getLogger(TimerSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Avslutter timedlige oppgaver...");
            query.close();
        }
        };// Kjører hver time
        timer.schedule(hourlyTask, 0l, 1000*60*60);
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        start();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
