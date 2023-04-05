/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java2budgetproject;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author MayxT
 */
public class database {
       public static Connection connect() {
    try{
        Class.forName("com.mysql.jdbc.Driver");
        
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/budgetapp", "root", "");
        return connect; 
        
    }catch (Exception e){
        e.printStackTrace();
    }
    return null;
    }

    static void disconnect(Connection connect) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
