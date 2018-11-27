/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Klasser;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sondre
 */
public class Kjeks {

    public Kjeks() {
    }
    
    public void makeCookie (HttpServletRequest request, HttpServletResponse response)    {
        if (request.getParameter("huskMeg")!=null)   {
            Cookie kjeks = new Cookie("kjeks", request.getParameter("brukernavn"));
            kjeks.setMaxAge(24*60*60);  //Hvor lenge cookien blir lagret i sekunder (24*60*60 = 24 timer)
            response.addCookie(kjeks);
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
