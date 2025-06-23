/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.aplicacaovendedores;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.input.MouseEvent;
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
public class TelaDepartamentosController implements Initializable {

    @FXML
    private TableView<Departamento> tableViewDpto;

    @FXML
    private TableColumn<Departamento, Integer> tableColumnCod;
    @FXML
    private TableColumn<Departamento, String> tableColumnNome;

    private ObservableList<Departamento> listaTabela;

    @FXML
    private Button btnNovo;

    @FXML
    private Button btnExcluir;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // configurando cada coluna
        tableColumnCod.setCellValueFactory(new PropertyValueFactory<>("codDpto"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nomeDpto"));

        atualizarTabela();

        // implementando evento de duplo clique
        /*tableViewDpto.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2 && tableViewDpto.getSelectionModel().getSelectedItem() != null) {
                    Departamento dpto = tableViewDpto.getSelectionModel().getSelectedItem();
                    System.out.println(dpto.toString());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TelaCadastroDepartamento.fxml"));
                        Scene scene = new Scene(loader.load());
                        Stage stage = new Stage();
                        stage.setTitle("Cadastro de Departamento");
                        stage.setScene(scene);
                        //stage.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
                        stage.initOwner(btnNovo.getScene().getWindow());
                        stage.initModality(Modality.WINDOW_MODAL);
                        TelaCadastroDepartamentoController cont = loader.getController();
                        cont.setDpto(dpto);
                        System.out.println("esperando...");
                        stage.showAndWait();
                        System.out.println("voltei");

                        atualizarTabela();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });*/
        EventHandler<MouseEvent> cliqueMouse = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton().equals(MouseButton.PRIMARY)) {
                    if (t.getClickCount() == 2 && tableViewDpto.getSelectionModel().getSelectedItem() != null) {
                        Departamento dpto = tableViewDpto.getSelectionModel().getSelectedItem();
                        System.out.println(dpto.toString());
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("TelaCadastroDepartamento.fxml"));
                            Scene scene = new Scene(loader.load());

                            Stage stage = new Stage();
                            stage.setTitle("Cadastro de Departamento");
                            stage.setScene(scene);
                            //stage.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
                            stage.initOwner(btnNovo.getScene().getWindow());
                            stage.initModality(Modality.WINDOW_MODAL);
                            TelaCadastroDepartamentoController cont = loader.getController();
                            cont.setDpto(dpto);
                            System.out.println("entrei pelo evento do clique...");
                            stage.showAndWait();
                            System.out.println("voltei");

                            atualizarTabela();

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        };
        tableViewDpto.setOnMouseClicked(cliqueMouse);

        btnNovo.setOnAction((t) -> {
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("TelaCadastroDepartamento.fxml"));
                Scene scene = new Scene(parent);

                Stage stage = new Stage();
                stage.setTitle("Cadastro de Departamento");
                stage.setScene(scene);
                //stage.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
                stage.initOwner(btnNovo.getScene().getWindow());
                stage.initModality(Modality.WINDOW_MODAL);

                stage.showAndWait();
                atualizarTabela();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        btnExcluir.setOnAction((t) -> {
            if (tableViewDpto.getSelectionModel().getSelectedItem() != null) {
                Departamento dpto = tableViewDpto.getSelectionModel().getSelectedItem();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmação");
                alert.setHeaderText(null);
                alert.setContentText(dpto.getNomeDpto() + " será excluido! Tem certeza?");
                if (alert.showAndWait().get() == ButtonType.OK) {
                    if (new DepartamentoService().excluir(dpto)) {
                        Alert mens = new Alert(Alert.AlertType.INFORMATION);
                        mens.setTitle("Excluído");
                        mens.setHeaderText(null);
                        mens.setContentText("Registro excluído!");
                        mens.showAndWait();
                        atualizarTabela();
                    }
                }
            }

        });
    }

    public void atualizarTabela() {
        // associando lista a tabela utilizando um ObservableList
        listaTabela = FXCollections.observableArrayList(new DepartamentoService().getAll());
        tableViewDpto.setItems(listaTabela);

    }

}
