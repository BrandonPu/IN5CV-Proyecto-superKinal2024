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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.brandonpu.dao.Conexion;
import org.brandonpu.dto.EmpleadoDTO;
import org.brandonpu.model.Empleado;
import org.brandonpu.system.Main;
import org.brandonpu.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author Pavilión
 */
public class MenuEmpleadosController implements Initializable {
    private Main stage;
    
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    @FXML
    ComboBox cmbEncargadoId;
    @FXML
    TextField tfEmpleadoId,tfBuscarId;
    @FXML
    Button btnRegresar,btnGuardar,btnAgregar,btnEditar,btnVaciar,btnEliminar,btnBuscar;
    @FXML
    TableView tblEmpleados;
    @FXML
    TableColumn colEmpleadoId,colNombreEmpleado,colApellidoEmpleado,colSueldo,colHoraEntrada,colHoraSalida,colCargo,colEncargado;
   
    /**
     * Initializes the controller class.
     */
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            stage.menuPrincipalView();
        }else if(event.getSource() == btnAgregar){
            stage.formEmpleadosView(1);
        } else if(event.getSource() == btnEditar){
            EmpleadoDTO.getEmpleadoDTO().setEmpleado((Empleado)tblEmpleados.getSelectionModel().getSelectedItem());
            stage.formEmpleadosView(2);
        } else if(event.getSource() == btnVaciar){
            vaciarCampos();
        } else if(event.getSource() == btnGuardar){
            editarEncargado();
            cargarDatos();
        } else if(event.getSource() == btnEliminar){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(405).get() == ButtonType.OK){
                int empId = ((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getEmpleadoId();
                eliminarEmpleados(empId);
                cargarDatos();
                vaciarCampos();
            }
        } else if(event.getSource() == btnBuscar){
            tblEmpleados.getItems().clear();
            if(tfBuscarId.getText().equals("")){
                cargarDatos();
            } else{
                tblEmpleados.getItems().add(buscarEmpleado());
                colEmpleadoId.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("empleadoId"));
                colNombreEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombreEmpleado"));
                colApellidoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidoEmpleado"));
                colSueldo.setCellValueFactory(new PropertyValueFactory<Empleado, Double>("sueldo"));
                colHoraEntrada.setCellValueFactory(new PropertyValueFactory<Empleado, String>("horaEntrada"));
                colHoraSalida.setCellValueFactory(new PropertyValueFactory<Empleado, String>("horaSalida"));
                colCargo.setCellValueFactory(new PropertyValueFactory<Empleado, String>("cargo"));
                colEncargado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("encargado"));
            }
        }
    }
    
    public void vaciarCampos(){
        tfEmpleadoId.clear();
        cmbEncargadoId.getSelectionModel().clearSelection();
    }
    
    
    public void cargarDatos(){
        tblEmpleados.setItems(listarEmpleados());
        colEmpleadoId.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("empleadoId"));
        colNombreEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombreEmpleado"));
        colApellidoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidoEmpleado"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<Empleado, Double>("sueldo"));
        colHoraEntrada.setCellValueFactory(new PropertyValueFactory<Empleado, String>("horaEntrada"));
        colHoraSalida.setCellValueFactory(new PropertyValueFactory<Empleado, String>("horaSalida"));
        colCargo.setCellValueFactory(new PropertyValueFactory<Empleado, String>("cargo"));
        colEncargado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("encargado"));
        tblEmpleados.getSortOrder().add(colEmpleadoId);
    }
 
    public void cargarDatosEditar(){
        Empleado ep = (Empleado)tblEmpleados.getSelectionModel().getSelectedItem();
        if(ep != null){
            tfEmpleadoId.setText(Integer.toString(ep.getEmpleadoId()));
            cmbEncargadoId.getSelectionModel().select(ObtenerIndexEmpleado());
        }
    }
    
    public int ObtenerIndexEmpleado(){
        int index = 0;
        for(int i = 0 ; i < cmbEncargadoId.getItems().size() ; i++){ 
            String encargadoCmb = cmbEncargadoId.getItems().get(i).toString();
            String empleadoTbl = ((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getEncargado();
            if(encargadoCmb.equals(empleadoTbl)){
                index = i;
                break;
            }
        }
        return index;
    }
    
    public void editarEncargado(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_asignarEncargado(?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfEmpleadoId.getText()));
            statement.setInt(2, ((Empleado)cmbEncargadoId.getSelectionModel().getSelectedItem()).getEmpleadoId());
            statement.execute();
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
    
    public void eliminarEmpleados(int empId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_eliminarEmpleado(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, empId);
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
    
    public Empleado buscarEmpleado(){
        Empleado empleado = null;
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarEmpleado(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfBuscarId.getText()));
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                int empleadoId = resultSet.getInt("empleadoId");
                String nombreEmpleado = resultSet.getString("nombreEmpleado");
                String apellidoEmpleado = resultSet.getString("apellidoEmpleado");
                Double sueldo = resultSet.getDouble("sueldo");
                String horaEntrada = resultSet.getString("horaEntrada");
                String horaSalida = resultSet.getString("horaSalida");
                String cargoId = resultSet.getString("cargoId");
                String encargadoId = resultSet.getString("encargadoId");
                
                empleado = (new Empleado(empleadoId,nombreEmpleado,apellidoEmpleado,sueldo,horaEntrada,horaSalida,cargoId,encargadoId));
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
        return empleado;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbEncargadoId.setItems(listarEmpleados());
        cargarDatos();
                
    }    

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
}
