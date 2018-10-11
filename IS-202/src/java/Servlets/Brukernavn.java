package Servlets;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import Database.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.Integer;

public class Brukernavn {
    public static String fornavn;
    public static String etternavn;
    public static String firstnamechars;
    public static String lastnamechars;
    public static String dato = new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
    public static String Ã¥r;
    public String brukernavn; 
    
    public Brukernavn(HttpServletRequest request){
    fornavn=request.getParameter("fornavn");
    etternavn=request.getParameter("etternavn");
    
    if (fornavn.length() > 4)
            firstnamechars = fornavn.substring(0, 4);
    else firstnamechars = fornavn;
    
    if (fornavn.length() > 3)
        lastnamechars = etternavn.substring(0, 3);
    else lastnamechars = etternavn;
    
    Ã¥r = dato.substring(Math.max(dato.length() - 2, 0));
    brukernavn = firstnamechars + lastnamechars + Ã¥r;    
    sjekkBrukernavn();
    }
    
    public String getBrukernavn(){
        return brukernavn;
    }
    
    public void sjekkBrukernavn() {
        Query query = new Query();
        ResultSet rs = query.query("SELECT * FROM bruker where brukernavn like '"+brukernavn+"%'");
        try {
            if (rs.next()){
            rs.last();
            int tall = rs.getRow();
            System.out.println(tall);
            String Stringtall = Integer.toString(tall);
            System.out.println(Stringtall);
            brukernavn = brukernavn + Stringtall;
            }
        } catch (SQLException ignore) {
            //ignore
        }
    }
}
