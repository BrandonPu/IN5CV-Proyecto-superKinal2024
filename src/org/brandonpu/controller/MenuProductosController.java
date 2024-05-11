/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.brandonpu.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.brandonpu.system.Main;

/**
 * FXML Controller class
 *
 * @author Pavilión
 */
public class MenuProductosController implements Initializable {
    private Main stage;
    /**
     * Initializes the controller class.
     */
    @FXML
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO
    }    

    @FXML
    public void handleButtonAction(ActionEvent event){
        
    }
    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    
}
