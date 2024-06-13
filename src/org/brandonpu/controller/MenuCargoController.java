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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.brandonpu.dao.Conexion;
import org.brandonpu.model.Cargo;
import org.brandonpu.system.Main;
import org.brandonpu.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author Pavilión
 */
public class MenuCargoController implements Initializable {
    private Main stage;
    
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    /**
     * Initializes the controller class.
     */
    @FXML
    Button btnRegresar, btnGuardar, btnVaciar, btnEliminar, btnBuscar, btnEditar;
    @FXML
    TableView tblCargos;
    @FXML
    TableColumn colCargoId, colNombreCargo, colDescripcionCargo;
    @FXML
    TextArea taDescripcion;
    @FXML
    TextField tfNombreCargo, tfCargosId, tfCargoId;
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            stage.menuPrincipalView();
        } else if(event.getSource() == btnGuardar){
            if(tfCargosId.getText().equals("")){
                if(!tfNombreCargo.getText().equals("") && !taDescripcion.getText().equals("")){
                    agregarCargo();
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(401);
                    cargarDatos();
                } else{
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                    tfNombreCargo.requestFocus();
                    return;
                }
            } else{
                if(!tfCargosId.getText().equals("")){
                    if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(106).get() == ButtonType.OK){
                        if(!tfNombreCargo.getText().equals("") && !taDescripcion.getText().equals("")){
                            editarCargo();
                            cargarDatos();
                        } else{
                            SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                            tfNombreCargo.requestFocus();
                            return;
                        }
                    }
                }
            }
        } else if(event.getSource() == btnVaciar){
            vaciarCampos();
        }else if(event.getSource() == btnEliminar){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(405).get() == ButtonType.OK){
                int carId = ((Cargo)tblCargos.getSelectionModel().getSelectedItem()).getCargoId();
                eliminarCargo(carId);
                cargarDatos();
            }
        } else if(event.getSource() == btnBuscar){
            tblCargos.getItems().clear();
            if(tfCargoId.getText().equals("")){
                cargarDatos();
            }else{
                tblCargos.getItems().add(buscarCargo());
                colCargoId.setCellValueFactory(new PropertyValueFactory<Cargo, Integer>("cargoId"));
                colNombreCargo.setCellValueFactory(new PropertyValueFactory<Cargo, String>("nombreCargo"));
                colDescripcionCargo.setCellValueFactory(new PropertyValueFactory<Cargo, String>("descripcionCargo"));
            }
        }
    }
    
    public void vaciarCampos(){
        tfCargosId.clear();
        taDescripcion.clear();
        tfNombreCargo.clear();
    }
    
    public void cargarDatos(){
        tblCargos.setItems(listarCargos());
        colCargoId.setCellValueFactory(new PropertyValueFactory<Cargo, Integer>("cargoId"));
        colNombreCargo.setCellValueFactory(new PropertyValueFactory<Cargo, String>("nombreCargo"));
        colDescripcionCargo.setCellValueFactory(new PropertyValueFactory<Cargo, String>("descripcionCargo"));
        tblCargos.getSortOrder().add(colCargoId);
    }
    
    public void cargarDatosEditar(){
        Cargo cr = (Cargo)tblCargos.getSelectionModel().getSelectedItem();
        if(cr != null){
            tfCargosId.setText(Integer.toString(cr.getCargoId()));
            tfNombreCargo.setText(cr.getNombreCargo());
            taDescripcion.setText(cr.getDescripcionCargo());
            
        }
    }
    
    
    public ObservableList<Cargo> listarCargos(){
         ArrayList<Cargo> cargos = new ArrayList<>();
         try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarCargos()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int cargoId = resultSet.getInt("cargoId");
                String nombreCargo = resultSet.getString("nombreCargo");
                String descripcionCargo = resultSet.getString("descripcionCargo");
                
                cargos.add(new Cargo(cargoId, nombreCargo, descripcionCargo));
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
        return FXCollections.observableList(cargos);
    }
    
    public void agregarCargo(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarCargo(?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tfNombreCargo.getText());
            statement.setString(2, taDescripcion.getText());
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
    
    public void editarCargo(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarCargo(?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfCargosId.getText()));
            statement.setString(2, tfNombreCargo.getText());
            statement.setString(3, taDescripcion.getText());
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
    
    public void eliminarCargo(int carId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_eliminarCargo(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, carId);
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
    
     public Cargo buscarCargo(){
        Cargo cargo = null;
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarCargo(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfCargoId.getText()));
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                int cargoId = resultSet.getInt("cargoId");
                String nombreCargo = resultSet.getString("nombreCargo");
                String descripcionCargo = resultSet.getString("descripcionCargo");
                
                cargo = (new Cargo(cargoId, nombreCargo, descripcionCargo));
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
        return cargo;   
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }    

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    
    
}
