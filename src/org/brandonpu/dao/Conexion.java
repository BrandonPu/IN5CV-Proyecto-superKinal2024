/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.brandonpu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Pavilión
 */
public class Conexion {
    private static Conexion instance;
    
    private String url = "jdbc:mysql://localhost:3306/SuperKinalDB?serverTimezone=GMT-6&useSSL=false";
    private String user = "brandonPu"; //Cambiar el usuario para que funcione 
    private String password = "1234"; //Tambien la contraseña simpre de uno
    
    private Conexion(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
    }
    
    public static Conexion getInstance(){
        if(instance == null){
            instance = new Conexion();
        }
        return instance;
    }
    
    public Connection obtenerConexion()throws SQLException {
       return DriverManager.getConnection(url, user, password);
    }
}
