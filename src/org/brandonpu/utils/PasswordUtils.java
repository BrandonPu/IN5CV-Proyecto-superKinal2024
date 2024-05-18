/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.brandonpu.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Pavilión
 */
public class PasswordUtils {
    
    private static  PasswordUtils instance;
    
    private PasswordUtils(){
        
    }

    public static PasswordUtils getInstance() {
        if(instance == null){
            instance = new PasswordUtils();
        }
        return instance;
    }
    
    public String encrytedPassword(String pass){
        return BCrypt.hashpw(pass, BCrypt.gensalt());
    }
    
    public boolean checkPassword(String pass, String encrytedPass){
        return BCrypt.checkpw(pass, encrytedPass);
    } 
}
