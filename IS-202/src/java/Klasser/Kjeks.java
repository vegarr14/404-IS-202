/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Klasser;

import Servlets.Login;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

/**
 *
 * @author Sondre
 */
public class Kjeks {

    public Kjeks() {
    }
    
    public void makeCookie (HttpServletRequest request, HttpServletResponse response)    {
        try {
        if (request.getParameter("huskMeg")!=null)   {
            Cookie kjeks = new Cookie("kjeks", URLEncoder.encode(request.getParameter("brukernavn"), "UTF-8"));
            kjeks.setMaxAge(24*60*60);  //Hvor lenge cookien blir lagret i sekunder (24*60*60 = 24 timer)
            response.addCookie(kjeks);
            System.out.println("Kjeks 1: "+kjeks);
            System.out.println("KjeksGetName: "+kjeks.getName());
        }
        }   catch (UnsupportedEncodingException uEx) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, uEx);
        }
    }
    
    public void emptyCookie (HttpServletRequest request, HttpServletResponse response)    {
        if (request.getParameter("huskMeg")==null)   {
            System.out.println("Murmur er på veg, pass på rumpen din nu!");
        Cookie[] cookies = request.getCookies();
        for(int i = 0; i < cookies.length; i++) { 
            if (cookies[i].getName().equals("kjeks"))  {
                cookies[i].setValue("");
                response.addCookie(cookies[i]);
            }
        }
        }
    }
}


