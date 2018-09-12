/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.*;

/**
 *
 * @author Erlend Thorsen
 */
public class registrerKunde {
    

    public boolean alleredeKunde(String epost){
        /* if epost in database return true*/
        PreparedStatement checkEmail = null;
        String checkEmailString =
                "SELECT ID FROM kontaktinfo WHERE Epost = '" +epost+ "'";
                
        try{  
            Class.forName("com.mysql.cj.jdbc.Driver");  
            Connection con=DriverManager.getConnection(  
            "jdbc:mysql://localhost:3306/test","root","root");  
  
            checkEmail = con.prepareStatement(checkEmailString);
            ResultSet rs = checkEmail.executeQuery();
            if(rs.next()){
                con.close();
                System.out.println("Allerede kunde");
                return true;
            }else{
                con.close();
               System.out.println("Ikke kunde fra før");
               return false; 
            }    
        }catch(Exception e){ 
            System.out.println(e);
        }  
        return false;
    }
}