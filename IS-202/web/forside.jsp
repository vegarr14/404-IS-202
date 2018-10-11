<%-- 
    Document   : forside
    Created on : 26.sep.2018, 09:34:54
    Author     : Erlend Thorsen
--%>

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
        %>
        <div class='velkommen'>
            <h1>Under Construction</h1>
            <% out.println("<h1>Velkommen " + session.getAttribute("fornavn") + " " + session.getAttribute("etternavn") + " din id: " + id +"</h1>");%>
            <h2>Her kommmer det snart mye gøy<br>Vennligst sjekk igjen senere</h2>
        </div>
    </body>
</html>
