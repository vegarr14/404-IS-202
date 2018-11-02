/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Database.Query;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Erlend Thorsen
 */
@WebServlet(name = "OppdaterBrukerKurs", urlPatterns = {"/OppdaterBrukerKurs"})
public class OppdaterBrukerKurs extends HttpServlet {

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
        
        Query query = new Query();
        
        String kursId = request.getParameter("kursId");
        String[] idArray;
        String values;
        //Legger til studenter i kurset            
        if(request.getParameter("leggTilStudenter") != null){
            if(request.getParameterValues("studenterIkkeIKurs") == null){
                nullValue(response,kursId);
            }else{          
                idArray = request.getParameterValues("studenterIkkeIKurs");
                for (int i = 0; i < idArray.length; i++){
                    idArray[i] = "('"+kursId+"', '"+idArray[i]+"')";
                }
                values = String.join(",", idArray);

                query.update("INSERT INTO TarKurs values"+values+"");
                closeAndRedirect(response,kursId,query);
            }
        }
        //fjerner studenter i kurset
        else if(request.getParameter("fjernStudenter") != null){
            if(request.getParameterValues("studenterIKurs") == null){
                nullValue(response,kursId);
            }else{ 
                idArray = request.getParameterValues("studenterIKurs");

                values = String.join(", ", idArray);
                query.update("DELETE FROM TarKurs where studentId in ("+values+") AND kursId='"+kursId+"'");
                closeAndRedirect(response,kursId,query);
            }
        }
        //Legger til forelesere i kurset
        else if(request.getParameter("leggTilForelesere") != null){
            if(request.getParameterValues("forelesereIkkeIKurs") == null){
                nullValue(response,kursId);
            }else{ 
                idArray = request.getParameterValues("forelesereIkkeIKurs");
                for (int i = 0; i < idArray.length; i++){
                    idArray[i] = "('"+kursId+"', '"+idArray[i]+"')";
                }
                values = String.join(",", idArray);

                query.update("INSERT INTO ForeleserKurs values"+values+"");
                closeAndRedirect(response,kursId,query);
            }
        }
        //Fjerner forelesere i kurset
        else if(request.getParameter("fjernForelesere") != null){
            if(request.getParameterValues("forelesereIKurs") == null){
                nullValue(response,kursId);
            }else{ 
                idArray = request.getParameterValues("forelesereIKurs");

                values = String.join(", ", idArray);
                query.update("DELETE FROM ForeleserKurs where foreleserId in ("+values+") AND kursId='"+kursId+"'");
                
                closeAndRedirect(response,kursId,query);
            }
        }
         
    }
    
    private void nullValue(HttpServletResponse response, String kursId) throws IOException{
       System.out.println("requested value was null, sent redirect"); 
       response.sendRedirect("BrukerListeKurs?kursId="+kursId+"&redigerBrukere=true");
    }
    private void closeAndRedirect(HttpServletResponse response, String kursId, Query query ) throws IOException{
        query.close();
        //Sender bruker tilbake til oversikt
        response.sendRedirect("BrukerListeKurs?kursId="+kursId+"&redigerBrukere=true");
        
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
