<%-- 
    Document   : index
    Created on : 14.sep.2018, 09:42:45
    Author     : Erlend Thorsen
--%>
<%@page import="Klasser.Kjeks"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>SLIT</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="style\styleBody.css">
    </head>
    <body>
        <div class="velkommen">
            <h1>Velkommen til <span style="color: orange">SLIT</span></h1>
            <h2> En læringsplatform med få funksjoner</h2>
                <h3>Logg inn her</h3>
                <form action="Login" method="post"> 
                    <%String theKjeks = "";
                    boolean check = false;
                    if (request.getCookies()!=null) {   //Hvis cookies ligger i request
                        Cookie[] cookies = request.getCookies(); 
                        for(int i = 0; i < cookies.length; i++) { 
                            if (cookies[i].getName().equals("kjeks"))  {    //kjeks laget i 
                                theKjeks = cookies[i].getValue();
                                if (theKjeks!="")   {
                                    check = true;
                                }}}}%>
                    <label>Brukernavn:</label> 
                    <input class="field" type="text" name="brukernavn" placeholder="Eksempel: OlaNor15" value=<%=theKjeks%>><br>
                    <label>Passord:</label> 
                    <input class="field" type="password" name="passord" placeholder="Eksempel: Passord123"><br>
                    <%if (check==true)    {%>
                    <label name="huskMeg">Husk brukernavn </label><input class="checkbox" action="huskMeg" type="checkbox" name="huskMeg" value="Husk brukernavn" checked>
                    <%} else    {%>
                    <label name="huskMeg">Husk brukernavn </label><input class="checkbox" action="huskMeg" type="checkbox" name="huskMeg" value="Husk brukernavn">
                    <%}%>
                    </br>
                    <input class="button" formaction="Login" type="submit" name="loggInbtn" value="Logg inn">
                    </form>
                <%
                    if (request.getAttribute("loginResult") != null && request.getAttribute("loginResult") == "true") {
                %>
                <p style="color:red">Feil brukernavn eller passord. <br> Vennligst prøv igjen</p>
                    <%
                        }
                        if (request.getAttribute("missingStatus") != null && request.getAttribute("missingStatus") == "true") {
                    %>
                <p style="color:red">Innlogging er riktig men du er verken student eller foreleser i systemet<br>"You have no power here Gandalf the grey" - King Theoden</p>
                    <%}%>
        </div>
    </body>
</html>
