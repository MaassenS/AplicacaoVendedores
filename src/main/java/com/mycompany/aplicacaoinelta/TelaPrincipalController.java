package com.mycompany.aplicacaoinelta;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Sistema Inelta
 */
public class TelaPrincipalController implements Initializable {

    @FXML
    private Button btnProdutos;
    @FXML
    private Button btnDashboard;
    @FXML
    private Button btnRelatorios;
    @FXML
    private Button btnSair;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnProdutos.setOnAction((t) -> {
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("TelaProdutos.fxml"));
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.setTitle("Gestão de Produtos - Sistema Inelta");
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        btnDashboard.setOnAction((t) -> {
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("TelaDashboard.fxml"));
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.setTitle("Dashboard de Lucros - Sistema Inelta");
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        btnRelatorios.setOnAction((t) -> {
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("TelaRelatorios.fxml"));
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.setTitle("Relatórios de Vendas - Sistema Inelta");
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        btnSair.setOnAction((t) -> {
            Stage stage = (Stage) btnSair.getScene().getWindow();
            stage.close();
        });
    }
}