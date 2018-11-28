<%-- 
    Document   : forside
    Created on : 26.sep.2018, 09:34:54
    Author     : Erlend Thorsen
--%>
<%@page import="Klasser.Gjoremaal"%>
<%@page import="Klasser.Kjeks"%>
<%@page import="Database.KalenderHendelse"%>
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
        <%
            String id = (String)session.getAttribute("id");
            KalenderHendelse kh = new KalenderHendelse();
            String kalenderArray[][] = kh.getArray(id);
            if(kalenderArray != null){%>          
            <script type='text/javascript'>
                var i;
                var kalenderHendelser = new Array();               
                <%for(int i = 0; i < kalenderArray.length; i++){%>
                   <%out.print("i = "+i+";");%>
                   kalenderHendelser[i] = new Array(<%
                    for(int k = 0; k <= 2; k++){
                        out.print("\""+kalenderArray[i][k]+"\"");
                        if(k+1 <= 2){
                            out.print(",");
                        }
                    }%>);
                    console.log(kalenderHendelser[i][i])
                <%}%>
            </script>
            <%}%>
        <link rel='stylesheet' type='text/css' href='style/styleKalender.css'>
        <script type='text/javascript' src='Javascript/Kalender2.js'></script>
    </head>
    <body>
        <% 
            boolean isForeleser = (boolean) session.getAttribute("isForeleser");
            
            Kjeks kjeks = new Kjeks();
            kjeks.emptyCookie(request, response);
            kjeks.makeCookie(request, response);
        %>
        <div class='titleBox'>
            <% out.println("<h1>Velkommen " + session.getAttribute("fornavn") + " " + session.getAttribute("etternavn")+"</h1>");%> 
        </div>

        <div class='calDiv'>
            <input class='button' id='prevMonthButton' type='submit' name='prevMonthButton' value='Forrige måned'/>
            <input class='button' id='nextMonthButton' type='submit' name='nextMonthButton' value='Neste måned'/>
            <div id='calendar'></div>
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
            <%
                Navbar navbar = new Servlets.Navbar();
                navbar.printNavbarJSP("Forside", id, isForeleser, out);
            %>
    </body>
</html>
