/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.brandonpu.system;

import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.brandonpu.controller.AgregarDistribuidoresController;
import org.brandonpu.controller.MenuAgregarClientesController;
import org.brandonpu.controller.MenuClientesController;
import org.brandonpu.controller.FormDistribuidoresController;
import org.brandonpu.controller.FormEmpleadosController;
import org.brandonpu.controller.MenuCargoController;
import org.brandonpu.controller.MenuCategoriaProductosController;
import org.brandonpu.controller.MenuEmpleadosController;
import org.brandonpu.controller.MenuFacturaController;
import org.brandonpu.controller.MenuPrincipalController;
import org.brandonpu.controller.MenuProductosController;
import org.brandonpu.controller.MenuPromocionesController;
import org.brandonpu.controller.MenuTicketSoporteController;

/**
 *
 * @author Pavilión
 */
public class Main extends Application {
    private Stage stage;
    private Scene scene;
    private final String URLVIEW = "/org/brandonpu/view/";
    
    @Override
    public void start(Stage stage) throws Exception{
        this.stage = stage;
        Image icon = new Image("/org/brandonpu/image/icon.png");
        stage.setTitle("Super Kinal APP");
        stage.getIcons().add(icon);
        menuPrincipalView();
        stage.show();
    }
    
    public Initializable switchScene(String fxmlName, int width, int height) throws Exception{
        Initializable resultado;
        FXMLLoader loader = new FXMLLoader();
        
        InputStream file = Main.class.getResourceAsStream(URLVIEW + fxmlName);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(URLVIEW + fxmlName));
        
        scene = new Scene((AnchorPane)loader.load(file), width, height);
        stage.setScene(scene);
        stage.sizeToScene();
        
        resultado = (Initializable)loader.getController();
        return resultado;
    }
    
    public void menuPrincipalView(){
        try{
            MenuPrincipalController menuPrincipalView = (MenuPrincipalController)switchScene("MenuPrincipalView.fxml", 950, 700);
            menuPrincipalView.setStage(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void menuClientesView(){
        try{
            MenuClientesController menuClientesView = (MenuClientesController)switchScene("MenuClientesView.fxml", 1200, 750);
            menuClientesView.setStage(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void menuAgregarClientesView(int op){
        try{
            MenuAgregarClientesController menuAgregarView = (MenuAgregarClientesController)switchScene("MenuAgregarClientesView.fxml", 600, 700);
            menuAgregarView.setOp(op);
            menuAgregarView.setStage(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void menuTicketSoporteView(){
        try{
            MenuTicketSoporteController menuTicketSoporteView = (MenuTicketSoporteController)switchScene("MenuTicketSoporteView.fxml", 1200,750);
            menuTicketSoporteView.setStage(this);
        } catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void menuDistribuidorView(){
        try{
            FormDistribuidoresController menuDistribuidoresView = (FormDistribuidoresController)switchScene("FormDistribuidoresView.fxml", 1200,750);
            menuDistribuidoresView.setStage(this);      
        } catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void agregarDistribuidoresView(int op){
       try{
            AgregarDistribuidoresController agregarDistribuidoresView = (AgregarDistribuidoresController)switchScene("AgregarDistribuidoresView.fxml", 625,775);
            agregarDistribuidoresView.setOp(op);
            agregarDistribuidoresView.setStage(this);      
        } catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void menuCategoriaProductoView(){
        try{
            MenuCategoriaProductosController menuCategoriaProductosController = (MenuCategoriaProductosController)switchScene("MenuCategoriaProductosView.fxml", 1200,750);
            menuCategoriaProductosController.setStage(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        
    }
    
    public void menuCargoView(){
        try{
            MenuCargoController menuCargoView = (MenuCargoController)switchScene("MenuCargoView.fxml", 1200,750);
            menuCargoView.setStage(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void menuEmpleadosView(){
        try{
            MenuEmpleadosController menuEmpleadosView = (MenuEmpleadosController)switchScene("MenuEmpleadosView.fxml", 1300,850);
            menuEmpleadosView.setStage(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        
    }
    public void formEmpleadosView(int op){
        try{
            FormEmpleadosController formEmpleadosView = (FormEmpleadosController)switchScene("FormEmpleadosView.fxml", 625,775);
            formEmpleadosView.setOp(op);
            formEmpleadosView.setStage(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void menuFacturaView(){
        try{
            MenuFacturaController menuFacturaView = (MenuFacturaController)switchScene("MenuFacturaView.fxml", 1200,750);
            menuFacturaView.setStage(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void menuProductosView(){
        try{
            MenuProductosController menuProductosView = (MenuProductosController)switchScene("MenuProductosView.fxml", 1600,950);
            menuProductosView.setStage(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
              
    }
    
    public void menuPromocionesView(){
        try{
            MenuPromocionesController menuPromocionesView = (MenuPromocionesController)switchScene("MenuPromocionesView.fxml", 1200,700);
            menuPromocionesView.setStage(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
