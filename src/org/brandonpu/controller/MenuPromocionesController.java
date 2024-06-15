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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.brandonpu.dao.Conexion;
import org.brandonpu.model.Producto;
import org.brandonpu.model.Promocion;
import org.brandonpu.system.Main;
import org.brandonpu.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author Pavilión
 */
public class MenuPromocionesController implements Initializable {
    private Main stage;
    
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    /**
     * Initializes the controller class.
     */
    @FXML
    TextField tfPromocionId, tfPrecio, tfFechaInicio, tfFechaFinalizacion,tfBuscarId;   
    @FXML
    TextArea taDescripcion;   
    @FXML
    ComboBox cmbProducto; 
    @FXML
    TableView tblPromociones;   
    @FXML
    TableColumn colPromocionId, colPrecio, colDescripcion, colFechaInicio, colFechaFinalizacion, colProducto;
    @FXML
    Button btnGuardar, btnEliminar, btnRegresar, btnVaciar,btnBuscar;
  
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            stage.menuPrincipalView();
        }else if(event.getSource() == btnGuardar){
            if(tfPromocionId.getText().equals("")){
                if(cmbProducto.getValue() != null && !tfPrecio.getText().equals("") && !taDescripcion.getText().equals("") && !tfFechaFinalizacion.getText().equals("")){
                    agregarPromociones();
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(401);
                    cargarDatos();   
                }else{
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                    cmbProducto.requestFocus();
                    return;
                }
                
            }else{
                if(!tfPromocionId.getText().equals("")){
                    if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(106).get() == ButtonType.OK){
                        if(cmbProducto.getValue() != null && !tfPrecio.getText().equals("") && !taDescripcion.getText().equals("") && !tfFechaFinalizacion.getText().equals("")){
                            editarPromociones();
                            cargarDatos();
                        }else{
                            SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                            cmbProducto.requestFocus();
                            return;
                        }
                    }
                }
            }
        }else if(event.getSource() == btnVaciar){
            vaciarCampos();
        } else if(event.getSource() == btnEliminar){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(405).get() == ButtonType.OK){
                int promId = ((Promocion)tblPromociones.getSelectionModel().getSelectedItem()).getPromocionId();
                eliminarPromocion(promId);
                cargarDatos();
            }
        } else if(event.getSource() == btnBuscar){
            tblPromociones.getItems().clear();
            if(tfBuscarId.getText().equals("")){
                cargarDatos();
            } else{
                tblPromociones.getItems().add(buscarPromocion());
                colPromocionId.setCellValueFactory(new PropertyValueFactory<Promocion, Integer>("promocionId"));
                colPrecio.setCellValueFactory(new PropertyValueFactory<Promocion, Double>("precioPromocion"));
                colDescripcion.setCellValueFactory(new PropertyValueFactory<Promocion, String>("descripcionPromocion"));
                colFechaInicio.setCellValueFactory(new PropertyValueFactory<Promocion, String>("fechaInicio"));
                colFechaFinalizacion.setCellValueFactory(new PropertyValueFactory<Promocion, Integer>("fechaFinalizacion"));
                colProducto.setCellValueFactory(new PropertyValueFactory<Promocion, String>("producto"));
                tblPromociones.getSortOrder().add(colPromocionId);
            }
        }
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbProducto.setItems(listarProductos());
        cargarDatos();       
    }
    
    public ObservableList<Promocion> listarPromociones(){
        ArrayList<Promocion> promociones = new ArrayList<>();
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarPromociones()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int promocionId = resultSet.getInt("promocionId");
                Double precioPromocion = resultSet.getDouble("precioPromocion");
                String descripcionPromocion = resultSet.getString("descripcionPromocion");
                String fechaInicio = resultSet.getString("fechaInicio");
                String fechaFinalizacion = resultSet.getString("fechaFInalizacion");
                String nombreProducto = resultSet.getString("producto");
                               
                promociones.add(new Promocion(promocionId, precioPromocion, descripcionPromocion, fechaInicio, fechaFinalizacion, nombreProducto));
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
            }
        }   
        return FXCollections.observableList(promociones);
    }
    
    public void cargarDatos(){
        tblPromociones.setItems(listarPromociones());
        colPromocionId.setCellValueFactory(new PropertyValueFactory<Promocion, Integer>("promocionId"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<Promocion, Double>("precioPromocion"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Promocion, String>("descripcionPromocion"));
        colFechaInicio.setCellValueFactory(new PropertyValueFactory<Promocion, String>("fechaInicio"));
        colFechaFinalizacion.setCellValueFactory(new PropertyValueFactory<Promocion, Integer>("fechaFinalizacion"));
        colProducto.setCellValueFactory(new PropertyValueFactory<Promocion, String>("producto"));
        tblPromociones.getSortOrder().add(colPromocionId);
    }
    
    public void cargarDatosEditar(){
        Promocion pn = (Promocion)tblPromociones.getSelectionModel().getSelectedItem();
        if(pn != null){
            tfPromocionId.setText(Integer.toString(pn.getPromocionId()));
            tfPrecio.setText(String.valueOf(pn.getPrecioPromocion()));
            taDescripcion.setText(pn.getDescripcionPromocion());
            tfFechaInicio.setText(pn.getFechaInicio());
            tfFechaFinalizacion.setText(pn.getFechaFinalizacion());
            cmbProducto.getSelectionModel().select(obtenerIndexProducto());
        }
    }
    
    public int obtenerIndexProducto(){
        int index = 0;
        for(int i = 0 ; i < cmbProducto.getItems().size() ; i++){
            String productoCmb = cmbProducto.getItems().get(i).toString();
            String productoTbl = ((Promocion)tblPromociones.getSelectionModel().getSelectedItem()).getProducto();
            if(productoCmb.equals(productoTbl)){
                index = i;
                break;
            }
        }
        return index;
    }

    public int obtenerIndexPromocion() {
        int index = 0;

        if (!cmbProducto.getItems().isEmpty()) {
            for (int i = 0; i < cmbProducto.getItems().size(); i++) {
                String productoCmb = cmbProducto.getItems().get(i).toString();
                String PromocionesTbl = ((Promocion) tblPromociones.getSelectionModel().getSelectedItem()).getProducto();
                if (productoCmb.equals(PromocionesTbl)) {
                    index = i;
                    break;
                }
            }

        }
        return index;
    }
    
    public void agregarPromociones(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarPromocion(?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            //statement.setString(1,tfPrecio.getText());
            statement.setDouble(1, Double.parseDouble(tfPrecio.getText()));
            statement.setString(2, taDescripcion.getText());
            statement.setString(3, tfFechaFinalizacion.getText());
            statement.setInt(4, ((Producto)cmbProducto.getSelectionModel().getSelectedItem()).getProductoId());
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
    
    public void editarPromociones(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarPromocion(?, ?, ?, ?, ?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfPromocionId.getText()));
            statement.setDouble(2, Double.parseDouble(tfPrecio.getText()));
            statement.setString(3, taDescripcion.getText());
            statement.setString(4, tfFechaInicio.getText());
            statement.setString(5, tfFechaFinalizacion.getText());
            statement.setInt(6, ((Producto)cmbProducto.getSelectionModel().getSelectedItem()).getProductoId());
            statement.execute();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(conexion != null){
                conexion.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }
    
   public ObservableList<Producto> listarProductos(){
        ArrayList<Producto> productos = new ArrayList<>();
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_ListarProductos()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int productoId = resultSet.getInt("productoId");
                String nombre = resultSet.getString("nombreProducto");
                String descripcion = resultSet.getString("descripcionProducto");
                int stock = resultSet.getInt("cantidadStock");
                double unidad = resultSet.getDouble("precioVentaUnitario");
                double mayor = resultSet.getDouble("precioVentaMayor");
                String distribuidor = resultSet.getString("Distribuidor");
                String categoria = resultSet.getString("Categoria");

                productos.add(new Producto(productoId, nombre, descripcion, stock, unidad, mayor, distribuidor,categoria));
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
        
        return FXCollections.observableList(productos);
    }
    
    public void eliminarPromocion(int promId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_eliminarPromocion(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, promId);
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
    
    public Promocion buscarPromocion(){
        Promocion promocion = null;
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarPromocion(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfBuscarId.getText()));
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                int promocionId = resultSet.getInt("promocionId");
                Double precio = resultSet.getDouble("precioPromocion");
                String descripcion = resultSet.getString("descripcionPromocion");
                String fechaInicio = resultSet.getString("fechaInicio");
                String fechaFinalizacion = resultSet.getString("fechaFinalizacion");
                String producto = resultSet.getString("producto");
                promocion = (new Promocion(promocionId,precio,descripcion,fechaInicio,fechaFinalizacion,producto));
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
        return promocion;
    }
    public void vaciarCampos(){
        tfPromocionId.clear();
        tfPrecio.clear();
        taDescripcion.clear();
        tfFechaInicio.clear();
        tfFechaFinalizacion.clear();
        cmbProducto.getSelectionModel().clearSelection();      
    }
      
    
    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
}
