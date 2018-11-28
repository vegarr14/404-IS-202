<%-- 
    Document   : forside
    Created on : 26.sep.2018, 09:34:54
    Author     : Erlend Thorsen
--%>
<%@page import="Klasser.Gjoremaal"%>
<%@page import="Servlets.Kjeks"%>
<%@page import="NotifikasjonSystem.PrintNotifikasjoner"%>
<%@page import="Servlets.Navbar"%>
<%@page import="javax.servlet.http.HttpSession"%>

<%
    //Sjekker om Bruker er logget inn, hvis ikke sendes til innlogging
    session = request.getSession();
    if(session.getAttribute("loggedIn") != "true"){
        response.sendRedirect("login.jsp");
    }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Forside</title>
        <link rel='stylesheet' type='text/css' href='style/styleNavbar.css'>
        <link rel='stylesheet' type='text/css' href='style/styleBody.css'>
    </head>
    <body>
        <% 
            boolean isForeleser = (boolean) session.getAttribute("isForeleser");
            String id = (String)session.getAttribute("id");
            Navbar navbar = new Servlets.Navbar();
            navbar.printNavbarJSP("Forside", id, isForeleser, out);
            
            Kjeks kjeks = new Kjeks();
            kjeks.emptyCookie(request, response);
            kjeks.makeCookie(request, response);
        %>
        <div class='titleBox'>
            <% out.println("<h1>Velkommen " + session.getAttribute("fornavn") + " " + session.getAttribute("etternavn")+"</h1>");%> 
        </div>
        <div class='gjoremalDiv'>
            <h2>Dine gjøremål</h2>
            <hr>
            <% 
              Gjoremaal gm = new Gjoremaal();
              gm.printGjoremaal(id, out);
            %>
        </div>
        <div class='notifikasjoner' id='notRight'>    
           <% //Printer uleste notifikasjoner
               PrintNotifikasjoner printNotifikasjoner = new PrintNotifikasjoner();
              
                printNotifikasjoner.printUleste(id,"JSP",out, null);
            %> 
        </div>
    </body>
</html>
