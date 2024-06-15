/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.brandonpu.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import org.brandonpu.dao.Conexion;
import org.brandonpu.dto.ProductoDTO;
import org.brandonpu.model.CategoriaProducto;
import org.brandonpu.model.Distribuidor;
import org.brandonpu.model.Producto;
import org.brandonpu.report.GenerarReporte;
import org.brandonpu.system.Main;
import org.brandonpu.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author Pavilión
 */
public class MenuProductoController implements Initializable {
    private Main stage;
    private int op;
        
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    private List<File> files = null;
    private Image baseImage;
    
    @FXML
    ComboBox cmbDistribuidores, cmbCategorias;
    @FXML
    Button btnRegresar,btnGuardar, btnBuscar, btnEliminar,btnVaciar,btnReportes,btnBuscarPro;
    @FXML
    TextField tfProductoId, tfNombreProducto,tfUnidad, tfMayor, tfCompra, tfDistribuidor, tfCategoria, tfStock,tfBuscarProductoId,tfBuscarId ;
    @FXML
    TableView tblProductos;
    @FXML
    TableColumn colProductoId,colNombre, colDescripcion, colUnidad, colMayor, colStock, colDistribuidor, colCategoria, colCompra;
    @FXML
    TextArea taDescripcionProducto;
    @FXML
    ImageView imgCargar, imgMostrar;
    @FXML
    Label lblNombreProducto, lblStock, lblUnitario, lblMayor, lblCompra;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbDistribuidores.setItems(listarDistribuidores());
        cmbCategorias.setItems(listarCategorias());
        baseImage = new Image("/org/brandonpu/image/FondoSubirImagen.png");
        imgCargar.setImage(baseImage);
        cargarDatos();
    }    

    @FXML
    public void handleButtonAction(ActionEvent event){
        try{
            if(event.getSource() == btnRegresar){
                stage.menuPrincipalView();
            }else if(event.getSource() == btnGuardar){
                if(tfProductoId.getText().equals("")){
                    if(!imgCargar.getImage().equals(baseImage) && cmbDistribuidores.getValue() != null && cmbCategorias.getValue() != null && !tfNombreProducto.getText().equals("") && !tfStock.getText().equals("") && !tfUnidad.getText().equals("") && !tfMayor.getText().equals("") &&  !tfCompra.getText().equals("") && !taDescripcionProducto.getText().equals("")){
                        agregarProducto();
                        SuperKinalAlert.getInstance().mostrarAlertaInfo(401);
                        cargarDatos();   
                    } else{
                        SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                        tfNombreProducto.requestFocus();
                        return;
                    }
                }else{
                    if(!tfProductoId.getText().equals("")){
                        if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(106).get() == ButtonType.OK){
                            if(!imgCargar.getImage().equals(baseImage) && cmbDistribuidores.getValue() != null && cmbCategorias.getValue() != null && !tfNombreProducto.getText().equals("") && !tfStock.getText().equals("") && !tfUnidad.getText().equals("") && !tfMayor.getText().equals("") &&  !tfCompra.getText().equals("") && !taDescripcionProducto.getText().equals("")){
                                editarProducto();
                                cargarDatos();   
                            } else{
                                SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                                tfNombreProducto.requestFocus();
                                return;
                            }
                        }
                    }
                }
            }else if(event.getSource() == btnBuscar){
                Producto producto = buscarProducto(Integer.parseInt(tfProductoId.getText()));
                if(producto != null){
                    lblNombreProducto.setText(producto.getNombreProducto());
                    lblStock.setText(Integer.toString(producto.getCantidadStock()));
                    lblUnitario.setText(Double.toString(producto.getPrecioVentaUnitario()));
                    lblMayor.setText(Double.toString(producto.getPrecioVentaMayor()));
                    lblCompra.setText(Double.toString(producto.getPrecioCompra()));
                    InputStream file = producto.getImagenProducto().getBinaryStream();
                    Image image = new Image(file);
                    imgMostrar.setImage(image);
                }
            }else if(event.getSource() == btnVaciar){
                vaciarCampos();
            } else if(event.getSource() == btnReportes){
                GenerarReporte.getInstance().generarProductos();
            } else if(event.getSource() == btnBuscarPro){
                tblProductos.getItems().clear();
                if(tfBuscarId.getText().equals("")){
                    cargarDatos();
                } else{
                    tblProductos.getItems().add(buscarProductoId());
                    colProductoId.setCellValueFactory(new PropertyValueFactory<Producto, Integer> ("productoId"));
                    colNombre.setCellValueFactory(new PropertyValueFactory<Producto, String> ("nombreProducto"));
                    colDescripcion.setCellValueFactory(new PropertyValueFactory<Producto, String> ("descripcionProductos"));
                    colStock.setCellValueFactory(new PropertyValueFactory<Producto, String> ("cantidadStock"));
                    colUnidad.setCellValueFactory(new PropertyValueFactory<Producto, String> ("precioVentaUnitario"));
                    colMayor.setCellValueFactory(new PropertyValueFactory<Producto, String> ("precioVentaMayor"));
                    colCompra.setCellValueFactory(new PropertyValueFactory<Producto, String> ("precioCompra"));
                    colDistribuidor.setCellValueFactory(new PropertyValueFactory<Producto, String> ("Distribuidor"));
                    colCategoria.setCellValueFactory(new PropertyValueFactory<Producto, String> ("categoriaProducto"));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @FXML
    public void handleOnDrag(DragEvent event){
        if(event.getDragboard().hasFiles()){
            event.acceptTransferModes(TransferMode.ANY);
        }
    }
    
    @FXML
    public void handleOnDrop(DragEvent event){
        try{
            files = event.getDragboard().getFiles();
            FileInputStream file = new  FileInputStream(files.get(0));
            Image image = new Image(file);    
            imgCargar.setImage(image);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void vaciarCampos(){
        tfProductoId.clear();
        tfNombreProducto.clear();
        tfStock.clear();
        tfUnidad.clear();
        tfMayor.clear();
        tfCompra.clear();
        taDescripcionProducto.clear();
        cmbDistribuidores.getSelectionModel().clearSelection();
        cmbCategorias.getSelectionModel().clearSelection();
        imgCargar.setImage(baseImage);
    }
    
    public void cargarDatosEditar(){
        Producto pd = buscarProducto(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getProductoId());
        if(pd != null){
            tfProductoId.setText(Integer.toString(pd.getProductoId()));
            tfNombreProducto.setText(pd.getNombreProducto());
            tfStock.setText(Integer.toString(pd.getCantidadStock()));
            tfUnidad.setText(Double.toString(pd.getPrecioVentaUnitario()));
            tfMayor.setText(Double.toString(pd.getPrecioVentaMayor()));
            tfCompra.setText(Double.toString(pd.getPrecioCompra()));
            taDescripcionProducto.setText(pd.getDescripcionProductos());
            cmbDistribuidores.getSelectionModel().select(obtenerIndexDistribuidor());
            cmbCategorias.getSelectionModel().select(obtenerIndexCategoria());
            try{
                InputStream file = pd.getImagenProducto().getBinaryStream();
                Image image = new Image(file);
                imgCargar.setImage(image);
            }catch(Exception e){
                e.printStackTrace();
            }
            ProductoDTO.getProductoDTO().setProducto(pd);
        }
    }
   
    public int obtenerIndexDistribuidor(){
        int index = 0;
        for(int i = 0 ; i <= cmbDistribuidores.getItems().size() ; i++){
            String distribuidorCmb = cmbDistribuidores.getItems().get(i).toString();
            String distribuidorTbl = ((Producto)tblProductos.getSelectionModel().getSelectedItem()).getDistribuidor();
            if(distribuidorCmb.equals(distribuidorTbl)){
                index = i;
                break;
            }
        }
        return index;
    }
    
    public int obtenerIndexCategoria(){
        int index = 0;
        for(int i = 0 ; i <= cmbCategorias.getItems().size() ; i++){
            String categoriaCmb = cmbCategorias.getItems().get(i).toString();
            String categoriaTbl = ((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCategoriaProducto();
            if(categoriaCmb.equals(categoriaTbl)){
                index = i;
                break;
            }
        }
        return index;
    }
    
    public void cargarDatos(){
        tblProductos.setItems(listarProductos());
        colProductoId.setCellValueFactory(new PropertyValueFactory<Producto, Integer> ("productoId"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Producto, String> ("nombreProducto"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Producto, String> ("descripcionProductos"));
        colStock.setCellValueFactory(new PropertyValueFactory<Producto, String> ("cantidadStock"));
        colUnidad.setCellValueFactory(new PropertyValueFactory<Producto, String> ("precioVentaUnitario"));
        colMayor.setCellValueFactory(new PropertyValueFactory<Producto, String> ("precioVentaMayor"));
        colCompra.setCellValueFactory(new PropertyValueFactory<Producto, String> ("precioCompra"));
        colDistribuidor.setCellValueFactory(new PropertyValueFactory<Producto, String> ("distribuidor"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<Producto, String> ("categoriaProducto"));
        tblProductos.getSortOrder().add(colProductoId);

    }
    
    public void editarProducto(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarProducto(?,?,?,?,?,?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            
            statement.setInt(1, Integer.parseInt(tfProductoId.getText()));
            statement.setString(2, tfNombreProducto.getText());
            if (taDescripcionProducto.getText().isEmpty()){
                statement.setString(3, null);
            } else{
                statement.setString(3, taDescripcionProducto.getText());
            }
            statement.setInt(4, Integer.parseInt(tfStock.getText()));
            statement.setDouble(5, Double.parseDouble(tfUnidad.getText()));
            statement.setDouble(6, Double.parseDouble(tfMayor.getText()));
            statement.setDouble(7, Double.parseDouble(tfCompra.getText()));
            if(imgCargar.getImage() == null){
                statement.setBinaryStream(8, null);
            } else {
                if(files != null){
                    InputStream img = new FileInputStream(files.get(0));
                    statement.setBinaryStream(8, img);
                }else{
                    statement.setBlob(8, ProductoDTO.getProductoDTO().getProducto().getImagenProducto());
                }
            }
            statement.setInt(9, ((Distribuidor) cmbDistribuidores.getSelectionModel().getSelectedItem()).getDistribuidorId());
            statement.setInt(10, ((CategoriaProducto) cmbCategorias.getSelectionModel().getSelectedItem()).getCategoriaProductoId());
            statement.execute();
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
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
                double compra = resultSet.getDouble("precioCompra");
                Blob imagen = resultSet.getBlob("imagenProducto");
                String distribuidor = resultSet.getString("Distribuidor");
                String categoria = resultSet.getString("Categoria");

                productos.add(new Producto(productoId, nombre, descripcion, stock, unidad, mayor, compra, imagen,distribuidor,categoria));
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
     
    public void agregarProducto(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarProducto(?,?,?,?,?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tfNombreProducto.getText());
            statement.setString(2, taDescripcionProducto.getText());
            statement.setString(3, tfStock.getText());
            statement.setString(4, tfUnidad.getText());
            statement.setString(5, tfMayor.getText());
            statement.setString(6, tfCompra.getText());
            InputStream img = new FileInputStream(files.get(0));
            statement.setBinaryStream(7,img);
            statement.setInt(8,((Distribuidor)cmbDistribuidores.getSelectionModel().getSelectedItem()).getDistribuidorId());
            statement.setInt(9,((CategoriaProducto)cmbCategorias.getSelectionModel().getSelectedItem()).getCategoriaProductoId());
            statement.execute();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(statement != null){
                    statement.close();
                }
                if(conexion != null){
                    conexion.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public ObservableList<Distribuidor> listarDistribuidores(){
        ArrayList<Distribuidor> distribuidores = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarDistribuidores()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while (resultSet.next()){
                int distribuidorId = resultSet.getInt("distribuidorId");
                String nombreDistribuidor = resultSet.getString("nombreDistribuidor");
                String direccionDistribuidor = resultSet.getString("direccionDistribuidor");
                String nitDistribuidor = resultSet.getString("nitDistribuidor");
                String telefonoDistribuidor = resultSet.getString("telefonoDistribuidor");
                String web = resultSet.getString("web");
                
                distribuidores.add(new Distribuidor(distribuidorId,nombreDistribuidor,direccionDistribuidor,nitDistribuidor,telefonoDistribuidor,web));
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
        
        return FXCollections.observableList(distribuidores);
    }   
    
    public ObservableList<CategoriaProducto> listarCategorias(){
        ArrayList<CategoriaProducto> categorias = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarCategoriaProductos()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int categoriaProductosId = resultSet.getInt("categoriaProductosId");
                String nombreCategoria = resultSet.getString("nombreCategoria");
                String descripcionCategoria = resultSet.getString("descripcionCategoria");
                
                categorias.add(new CategoriaProducto(categoriaProductosId,nombreCategoria,descripcionCategoria));
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
        return FXCollections.observableList(categorias);
    }
     
    public Producto buscarProducto(int prodId){
        Producto producto = null;
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_BuscarProducto(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, prodId);
            
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
               int productoId =  resultSet.getInt("productoId");
               String nombre = resultSet.getString("nombreProducto");
               String descripcionProducto = resultSet.getString("descripcionProducto");
               int stock =  resultSet.getInt("cantidadStock");
               double  unitario = resultSet.getDouble("precioVentaUnitario");
               double  mayor = resultSet.getDouble("precioVentaMayor");
               double  compra = resultSet.getDouble("precioCompra");
               Blob imagenProducto = resultSet.getBlob("imagenProducto");
               
               
               producto = new Producto(productoId, nombre, descripcionProducto, stock,unitario,mayor,compra, imagenProducto);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(conexion != null){
                    conexion.close();
                }else if(statement != null){
                    statement.close();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return producto;
    }
    
    public Producto buscarProductoId(){
        Producto producto = null;
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarProductoId(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfBuscarId.getText()));
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
               int productoId =  resultSet.getInt("productoId");
               String nombre = resultSet.getString("nombreProducto");
               String descripcionProducto = resultSet.getString("descripcionProducto");
               int stock =  resultSet.getInt("cantidadStock");
               double  unitario = resultSet.getDouble("precioVentaUnitario");
               double  mayor = resultSet.getDouble("precioVentaMayor");
               double  compra = resultSet.getDouble("precioCompra");
               String distribuidor = resultSet.getString("Distribuidor");
               String categoria = resultSet.getString("categoria");
               
               producto = new Producto(productoId, nombre, descripcionProducto, stock,unitario,mayor,compra, distribuidor,categoria);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(conexion != null){
                    conexion.close();
                }else if(statement != null){
                    statement.close();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return producto;
    }
    
    public void mostrarImagen() {
        Producto p = (Producto) tblProductos.getSelectionModel().getSelectedItem();
        if (p != null) {
            Blob img = p.getImagenProducto();
            if (img != null) {
                try {
                    InputStream inputStream = img.getBinaryStream();
                    Image image = new Image(inputStream);
                    imgMostrar.setImage(image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                imgMostrar.setImage(null);
            }
        }else{
            imgMostrar.setImage(null);
        }
    }
   
    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
}
