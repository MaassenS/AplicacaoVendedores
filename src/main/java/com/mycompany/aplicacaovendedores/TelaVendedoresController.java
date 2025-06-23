/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.aplicacaovendedores;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.classes.Departamento;
import model.classes.Vendedor;
import model.services.DepartamentoService;
import model.services.VendedorService;

/**
 * FXML Controller class
 *
 * @author herrmann
 */
public class TelaVendedoresController implements Initializable {

    @FXML
    private Button btnDepartamentos;

    @FXML
    private TableView<Vendedor> tableViewVend;

    @FXML
    private TableColumn<Vendedor, Integer> tableColumnCod;
    @FXML
    private TableColumn<Vendedor, String> tableColumnNome;
    @FXML
    private TableColumn<Vendedor, LocalDate> tableColumnData;
    @FXML
    private TableColumn<Vendedor, Double> tableColumnSalario;
    @FXML
    private TableColumn<Vendedor, String> tableColumnDepartamento;

    private ObservableList<Vendedor> listaTabela;

    
    @FXML
    private Button btnNovo;

    @FXML
    private Button btnExcluir;

    @FXML
    public void onBtnNovoAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TelaCadastroVendedor.fxml"));
            Pane pane = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Cadastro de Vendedor");
            stage.setScene(new Scene(pane));
            //stage.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
            stage.initOwner(btnNovo.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);

            stage.showAndWait();
            atualizarTabela();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    public void onBtnExcluirAction(ActionEvent event) {
        if (tableViewVend.getSelectionModel().getSelectedItem() != null) {
            Vendedor vend = tableViewVend.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação");
            alert.setHeaderText(null);
            alert.setContentText(vend.getNomeVendedor()+ " será excluido! Tem certeza?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                if (new VendedorService().excluir(vend)) {
                    Alert mens = new Alert(Alert.AlertType.INFORMATION);
                    mens.setTitle("Excluído");
                    mens.setHeaderText(null);
                    mens.setContentText("Registro excluído!");
                    mens.showAndWait();
                    atualizarTabela();
                }
            }
        }

    }    
    
    
    @FXML
    public void onBtnDepartamentos(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("TelaDepartamentos.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setTitle("Consulta de Departamentos");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // configurando cada coluna
        tableColumnCod.setCellValueFactory(new PropertyValueFactory<>("codVendedor"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nomeVendedor"));

        tableColumnData.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
            Utils.formatTableColumnDate(tableColumnData);
        tableColumnSalario.setCellValueFactory(new PropertyValueFactory<>("salarioBase"));
            Utils.formatTableColumnDouble(tableColumnSalario, 2);
        tableColumnDepartamento.setCellValueFactory(new PropertyValueFactory<>("nomeDpto"));

        atualizarTabela();
        
        // implementando evento de duplo clique
        tableViewVend.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2 && tableViewVend.getSelectionModel().getSelectedItem() != null) {
                    Vendedor vend = tableViewVend.getSelectionModel().getSelectedItem();
                    System.out.println(vend.toString());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("TelaCadastroVendedor.fxml"));
                        Pane pane = loader.load();

                        Stage stage = new Stage();
                        stage.setTitle("Cadastro de Departamento");
                        stage.setScene(new Scene(pane));
                        //stage.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
                        stage.initOwner(btnNovo.getScene().getWindow());
                        stage.initModality(Modality.WINDOW_MODAL);
                        TelaCadastroVendedorController cont = loader.getController();
                        cont.setVendedor(vend);
                        System.out.println("esperando...");
                        stage.showAndWait();
                        System.out.println("voltei");

                        atualizarTabela();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
    
    public void atualizarTabela(){
      // associando lista a tabela utilizando um ObservableList
        listaTabela = FXCollections.observableArrayList(new VendedorService().getAll());
        tableViewVend.setItems(listaTabela);
        
    }

}
