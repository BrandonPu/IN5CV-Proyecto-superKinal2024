/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.brandonpu.utils;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author informatica
 */
public class SuperKinalAlert {
    private static SuperKinalAlert instance;
    
    private SuperKinalAlert(){
    }
    
    public static SuperKinalAlert getInstance(){
        if(instance == null){
            instance = new SuperKinalAlert();
        }
        return instance;
    }
    
    
    public void mostrarAlertaInfo(int code){
        if(code == 400){ //ALERTA DE CAMPOS PENDIENTES DE LLENAR
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campos Pendientes");
            alert.setHeaderText("Campos pendientes");
            alert.setContentText("Algunos campos necesarios para el registro están pendientes");
            alert.showAndWait();
        }else if(code == 401){ //Alerta confirmacion de creacion de registro
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmación de registro");
            alert.setHeaderText("Confirmación de registro");
            alert.setContentText("El registro de ha creado con éxito!");
            alert.showAndWait();
        } else if(code == 602){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Usuario Incorrecto");
            alert.setHeaderText("Usuario Incorrecto");
            alert.setContentText("Verifique el usuario");
            alert.showAndWait();
        }else if(code == 005){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Contraseña incorrecta");
            alert.setHeaderText("Contraseña incorrecta");
            alert.setContentText("Verifique la Contraseña");
            alert.showAndWait();
        }else if(code == 45){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al Eliminar");
            alert.setHeaderText("Error al Eliminar");
            alert.setContentText("No se puede eliminar porque esta ligada a otro Objeto");
            alert.showAndWait();
        }
    }
    
    public Optional<ButtonType> mostrarAlertaConfirmacion(int code){
        Optional<ButtonType> action = null;
        if(code == 405){ //Alerta confirmacion para eliminación de registros
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminación de registro");
            alert.setHeaderText("Eliminación de registro");
            alert.setContentText("¿Desea confirmar la eliminación del regitro?");
            action = alert.showAndWait();
        }else if(code == 106){ //Alerta confirmacion para edición de registros
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Edición de registros");
            alert.setHeaderText("Edición de registros");
            alert.setContentText("¿Desea confirmar la edición del registro?");
            action = alert.showAndWait();
        }
        return action;
    }
    
    public void alertaSaludo(String usuario){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bienvenido");
        alert.setHeaderText("Bienvenido: " + usuario);
        alert.showAndWait();
    }
}
