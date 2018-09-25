<%-- 
    Document   : index
    Created on : 14.sep.2018, 09:42:45
    Author     : Erlend Thorsen
--%>

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
            <h2> Et læringsplatform med få funksjoner</h2>
            <div>
                <h3>Logg inn her</h3>
                <form action="Login" method="post">
                    <label>Brukernavn:</label>  
                    <input class="field" type="text" name="brukernavn"><br>
                    <label>Passord:</label> 
                    <input class="field" type="password" name="passord"><br>
                    <input class="button" formaction="Login" type="submit" name="loggInbtn" value="Logg inn">
                </form>
                    <%
                        if(request.getAttribute("loginResult") != null && request.getAttribute("loginResult") == "true"){
                    %>
                    <p style="color:red">Feil brukernavn eller passord. <br> Vennligst prøv igjen</p>
                    <%
                    }
                    %>
            </div>
        </div>
    </body>
</html>
