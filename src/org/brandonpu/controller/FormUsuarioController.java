/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.brandonpu.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.brandonpu.dao.Conexion;
import org.brandonpu.model.Empleado;
import org.brandonpu.model.NivelAcceso;
import org.brandonpu.system.Main;
import org.brandonpu.utils.PasswordUtils;

/**
 * FXML Controller class
 *
 * @author Pavilión
 */
public class FormUsuarioController implements Initializable {
    private Main stage;
    
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    /**
     * Initializes the controller class.
     */
    @FXML
    TextField tfUser, tfPassword;
    @FXML
    ComboBox cmbEmpleado,cmbNivelAcceso;
    @FXML
    Button btnRegistrar, btnEmpleado;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbEmpleado.setItems(listarEmpleados());
        cmbNivelAcceso.setItems(listarNivelAcceso());
    }    

     @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegistrar){
            agregarUsuario();
            stage.loginView();
        } else if(event.getSource() == btnEmpleado){
            stage.formEmpleadosView(3);
        }
    } 
    
    public ObservableList<NivelAcceso> listarNivelAcceso(){
        ArrayList<NivelAcceso> nivelesAcceso = new ArrayList<>();
        
        try{
           conexion = Conexion.getInstance().obtenerConexion();
           String sql = "call sp_listarNivelAcceso()";
           statement = conexion.prepareStatement(sql);
           resultSet = statement.executeQuery();
           
            while(resultSet.next()){
               int nivelAccesoId = resultSet.getInt("nivelAccesoId");
               String nivelAcceso = resultSet.getString("nivelAcceso");
               
               nivelesAcceso.add(new NivelAcceso(nivelAccesoId,nivelAcceso));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(resultSet != null){
                    resultSet.close();
                }
                if(statement != null){
                    statement.close();
                }
                if(conexion != null){
                    conexion.close();
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        return FXCollections.observableList(nivelesAcceso);
    }
    
    public ObservableList<Empleado> listarEmpleados(){
        ArrayList<Empleado> empleados = new ArrayList<>();
        
        try{
           conexion = Conexion.getInstance().obtenerConexion();
           String sql = "call sp_listarEmpleados()";
           statement = conexion.prepareStatement(sql);
           resultSet = statement.executeQuery();
           while(resultSet.next()){
               int empleadoId = resultSet.getInt("empleadoId");
               String nombreEmpleado = resultSet.getString("nombreEmpleado");
               String apellidoEmpleado = resultSet.getString("apellidoEmpleado");
               Double sueldo = resultSet.getDouble("sueldo");
               String horaEntrada = resultSet.getString("horaEntrada");
               String horaSalida = resultSet.getString("horaSalida");
               String cargo = resultSet.getString("cargo");
               String encargado = resultSet.getString("encargado");
               
               empleados.add(new Empleado(empleadoId,nombreEmpleado,apellidoEmpleado,sueldo,horaEntrada,horaSalida,cargo,encargado));
           }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(resultSet != null){
                    resultSet.close();
                }
                if(statement != null){
                    statement.close();
                }
                if(conexion != null){
                    conexion.close();
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        
        return FXCollections.observableList(empleados);
    }
    
    public void agregarUsuario(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarUsuario(?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tfUser.getText());
            statement.setString(2, PasswordUtils.getInstance().encrytedPassword(tfPassword.getText()));
            statement.setInt(3, ((NivelAcceso)cmbNivelAcceso.getSelectionModel().getSelectedItem()).getNivelAccesoId());
            statement.setInt(4, ((Empleado)cmbEmpleado.getSelectionModel().getSelectedItem()).getEmpleadoId());
            statement.execute();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(statement != null){
                    statement.close();
                }
                if(conexion != null){
                    conexion.close();
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }
   
    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
}
