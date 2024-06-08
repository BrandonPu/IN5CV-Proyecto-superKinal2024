/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.brandonpu.report;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.brandonpu.dao.Conexion;
import win.zqxu.jrviewer.JRViewerFX;

/**
 *
 * @author informatica
 */
public class GenerarReporte {
    private static GenerarReporte instance;
    
    private static Connection conexion = null;
    
    public GenerarReporte() {
    }

    public static GenerarReporte getInstance() {
        if(instance == null){
            instance = new GenerarReporte();
        }
        return instance;
    }
    
    public void generarFactura(int factId){
        try{
            // 1 abrir conexion a DB
            conexion = Conexion.getInstance().obtenerConexion();
            
            // 2 Defino los parametros del reporte
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("factId", factId);
            
            // 3 crear el reporte
            InputStream jasperPath = GenerarReporte.class.getResourceAsStream("/org/brandonpu/report/Factura.jasper");
            JasperPrint reporte = JasperFillManager.fillReport(jasperPath, parametros, conexion);
            
            // 4 Crear la ventana javafx para mostrar Reporte
            Stage reportStage = new Stage();
            
            // 5 Llenar el stage con el reporte
            JRViewerFX reportViewer = new JRViewerFX(reporte);
            
            Pane root = new Pane();
            
            root.getChildren().add(reportViewer);
            
            reportViewer.setPrefSize(1500, 900);
            
            Scene scene = new Scene(root);
            reportStage.setScene(scene);
            reportStage.setTitle("Factura");
            reportStage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void generarClientes(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            
            // 2 Defino los parametros del reporte
            Map<String, Object> parametros = new HashMap<>();
   
            InputStream jasperPath = GenerarReporte.class.getResourceAsStream("/org/brandonpu/report/Clientes.jasper");
            JasperPrint reporte = JasperFillManager.fillReport(jasperPath,parametros, conexion);
            
            // 4 Crear la ventana javafx para mostrar Reporte
            Stage reportStage = new Stage();
            
            // 5 Llenar el stage con el reporte
            JRViewerFX reportViewer = new JRViewerFX(reporte);
            
            Pane root = new Pane();
            
            root.getChildren().add(reportViewer);
            
            reportViewer.setPrefSize(1500, 900);
            
            Scene scene = new Scene(root);
            reportStage.setScene(scene);
            reportStage.setTitle("Cliente");
            reportStage.show();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void generarProductos(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            
            // 2 Defino los parametros del reporte
            Map<String, Object> parametros = new HashMap<>();
   
            InputStream jasperPath = GenerarReporte.class.getResourceAsStream("/org/brandonpu/report/Producto.jasper");
            JasperPrint reporte = JasperFillManager.fillReport(jasperPath,parametros, conexion);
            
            // 4 Crear la ventana javafx para mostrar Reporte
            Stage reportStage = new Stage();
            
            // 5 Llenar el stage con el reporte
            JRViewerFX reportViewer = new JRViewerFX(reporte);
            
            Pane root = new Pane();
            
            root.getChildren().add(reportViewer);
            
            reportViewer.setPrefSize(1500, 900);
            
            Scene scene = new Scene(root);
            reportStage.setScene(scene);
            reportStage.setTitle("Productos");
            reportStage.show();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
