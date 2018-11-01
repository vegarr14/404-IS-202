/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Josef
 */
@WebServlet(name = "Kalender", urlPatterns = {"/Kalender"})
public class Kalender extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>JavaScript calendar</title>");
            out.println("<link rel='stylesheet' type='text/css' href='style/styleKalender.css' />");
            out.println("<script type='text/javascript' src='Javascript/Kalender.js'></script>");
            out.println("</head>");
            out.println("<body onload='!function()'>");
            out.println("<div id='calendar'></div> ");

            /*out.println("<head>");
            out.println("<title>Servlet Kalender</title>");  
            out.println("<link rel='stylesheet' type='text/css' href='style/styleKalender.css' />");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>CSS Calendar</h1>");
            out.println("<div class='month'> ");
            out.println("<ul>");
            out.println("<li class='prev'>&#10094;</li>");
            out.println("<li class='next'>&#10095;</li>");
            out.println("<li>");
            out.println("August<br>");
            out.println("<span style='font-size:18px'>2078</span>");
            out.println("</li>");
            out.println("</ul>");
            out.println("</div>");

            out.println("<ul class='weekdays'>");
            out.println("<li>Mo</li>");
            out.println("<li>Tu</li>");
            out.println("<li>We</li>");
            out.println("<li>Th</li>");
            out.println("<li>Fr</li>");
            out.println("<li>Sa</li>");
            out.println("<li>Su</li>");
            out.println("</ul>");
            
            out.println("<ul class='days'>");
            out.println(" <li>1</li>");
            out.println(" <li>2</li>");
            out.println(" <li>3</li>");
            out.println(" <li>4</li>");
            out.println(" <li>5</li>");
            out.println(" <li>6</li>");
            out.println(" <li>7</li>");
            out.println(" <li>8</li>");
            out.println(" <li>9</li>");
            out.println(" <li>10</li>");
            out.println(" <li>11</li>");
            out.println(" <li>12</li>");
            out.println(" <li>13</li>");
            out.println(" <li>14</li>");
            out.println(" <li>15</li>");
            out.println(" <li>16</li>");
            out.println(" <li>17</li>");
            out.println(" <li>18</li>");
            out.println(" <li>19</li>");
            out.println(" <li>20</li>");
            out.println(" <li>21</li>");
            out.println(" <li>22</li>");
            out.println(" <li>23</li>");
            out.println(" <li>24</li>");
            out.println(" <li>25</li>");
            out.println(" <li>27</li>");
            out.println(" <li>28</li>");
            out.println(" <li>29</li>");
            out.println(" <li>30</li>");
            out.println(" <li>31</li>");
            
            out.println("</ul>");*/
            
            out.println("</body>");
            out.println("</html>");
            
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
