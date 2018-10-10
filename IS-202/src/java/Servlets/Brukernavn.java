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
    public static String firstname;
    public static String lastname;
    public static String firstnamechars;
    public static String lastnamechars;
    public static String dato = new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
    public static String Ã¥r;
    public String brukernavn; 
    
    public Brukernavn(HttpServletRequest request){
    firstname=request.getParameter("Fornavn");
    lastname=request.getParameter("Etternavn");
    
    if (firstname.length() > 4)
            firstnamechars = firstname.substring(0, 4);
    else firstnamechars = firstname;
    
    if (lastname.length() > 3)
        lastnamechars = lastname.substring(0, 3);
    else lastnamechars = lastname;
    
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
        query.close();
    }
    
    public static void main(String[] args) {}
}