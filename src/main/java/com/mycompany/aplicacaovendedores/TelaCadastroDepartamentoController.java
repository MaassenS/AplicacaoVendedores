/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.aplicacaovendedores;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.classes.Departamento;
import model.exceptions.ValidacaoException;
import model.services.DepartamentoService;

/**
 * FXML Controller class
 *
 * @author herrmann
 */
public class TelaCadastroDepartamentoController implements Initializable {

    private Departamento dpto;

    public void setDpto(Departamento dpto) {
        this.dpto = dpto;
        txtNome.setText(dpto.getNomeDpto());
        txtCodigo.setText(String.valueOf(dpto.getCodDpto()));
    }

    @FXML
    private Label lblErroNome;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnSair;

    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtNome;

    @FXML
    public void onBtnSalvarAction(ActionEvent event) {
        try {
            ValidacaoException exc = new ValidacaoException("Erro Validação de nome!");
            if (dpto == null) {
                dpto = new Departamento();
            }

            if (txtNome.getText() == null || txtNome.getText().equals("")) {
                exc.adicionarErro("Nome", "O campo não pode ser vazio!");
            } else {
                dpto.setNomeDpto(txtNome.getText());
            }

            if (!exc.getErrors().isEmpty()) {
                throw exc;
            }
            if (new DepartamentoService().salvarOuAtualizar(dpto)) {
                // fechando a tela
                Stage stage = (Stage) btnSair.getScene().getWindow();
                stage.close();
            } else {
                Alert al = new Alert(Alert.AlertType.ERROR);
                al.setTitle("ERRO");
                al.setHeaderText(null);
                al.setContentText("Ocorreu um erro ao salvar!");
                al.showAndWait();
            }
        } catch (ValidacaoException ev) {
            setErrorMessages(ev.getErrors());
        }
    }

    @FXML
    public void onBtnSairAction(ActionEvent event) {
        ((Stage) btnSair.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb
    ) {
        // TODO
    }

    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();
        lblErroNome.setText(fields.contains("Nome") ? errors.get("Nome") : "");
    }

}
