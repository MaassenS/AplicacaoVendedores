/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.aplicacaovendedores;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.UnaryOperator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import model.classes.Departamento;
import model.classes.Vendedor;
import model.exceptions.ValidacaoException;
import model.services.DepartamentoService;
import model.services.VendedorService;

/**
 * FXML Controller class
 *
 * @author herrmann
 */
public class TelaCadastroVendedorController implements Initializable {

    private List<Departamento> listaDepartamento;
    private ObservableList<Departamento> listaCombo;

    private Vendedor vend;

    public void setVendedor(Vendedor vend) {
        this.vend = vend;
        txtCodigo.setText(String.valueOf(vend.getCodVendedor()));
        txtNome.setText(vend.getNomeVendedor());
        dateNascimento.setValue(vend.getDataNascimento());
        txtSalario.setText(new DecimalFormat("0.00").format(vend.getSalarioBase()));
        cmbDepartamento.getSelectionModel().select(vend.getDpto());
    }

    @FXML
    private Label lblErroNome;
    @FXML
    private Label lblErroData;
    @FXML
    private Label lblErroSalario;
    @FXML
    private Label lblErroDepartamento;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnSair;

    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtNome;
    @FXML
    private DatePicker dateNascimento;
    @FXML
    private TextField txtSalario;
    @FXML
    private ComboBox<Departamento> cmbDepartamento;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listaDepartamento = new DepartamentoService().getAll();

        listaCombo = FXCollections.observableArrayList(listaDepartamento);
        // inicializando combobox
        cmbDepartamento.setItems(listaCombo);

        /*UnaryOperator<TextFormatter.Change> filter = new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change t) {
                String sep = Utils.getSeperadorDecimal();
                
                if (t.isReplaced()) 
                    if(t.getText().matches("[^0-9]"))
                        t.setText(t.getControlText().substring(t.getRangeStart(), t.getRangeEnd()));

                if (t.isAdded()) {
                    if (t.getControlText().contains(sep)) {
                        if (t.getText().matches("[^0-9]")) {
                            t.setText("");
                        }
                    } else if (t.getText().matches("[^0-9"+sep+"]")) {
                        t.setText("");
                    }
                }

                return t;
            }
        };
        txtSalario.setTextFormatter(new TextFormatter<>(filter));*/
        Utils.setTextFieldDouble(txtSalario);

        btnSalvar.setOnAction((t) -> {
            try {
                ValidacaoException exc = new ValidacaoException("Erro Validação!");
                if (vend == null) {
                    vend = new Vendedor();
                }

                if (txtNome.getText() == null) {
                    exc.adicionarErro("Nome", "O campo não pode ser vazio!");
                } else if (txtNome.getText().equals("")) {
                    exc.adicionarErro("Nome", "O campo não pode ser vazio!");
                } else {
                    vend.setNomeVendedor(txtNome.getText());
                }

                if (dateNascimento.getValue() == null) {
                    exc.adicionarErro("Data", "Data de Nascimento não pode ser vazia!");
                } else {
                    vend.setDataNascimento(dateNascimento.getValue());
                }

                if (txtSalario.getText() == null || txtSalario.getText().trim().equals("")) {
                    exc.adicionarErro("Salario", "Salário não pode ser vazio!");
                } else {
                    try {
                        //double valor = Double.parseDouble(txtSalario.getText());
                        double valor = Utils.tryParseToDouble(txtSalario.getText());
                        vend.setSalarioBase(valor);
                    } catch (Exception e) {
                        exc.adicionarErro("Salario", "Informe um salário válido!");
                    }
                }

                if (cmbDepartamento.getValue() == null) {
                    exc.adicionarErro("Departamento", "Informe um Departamento");
                } else {
                    vend.setDpto(cmbDepartamento.getValue());

                }

                if (!exc.getErrors().isEmpty()) {
                    throw exc;
                }

                if (new VendedorService().salvarOuAtualizar(vend)) {
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
        });

        btnSair.setOnAction((t) -> {
            ((Stage) btnSair.getScene().getWindow()).close();
        });

    }

    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();
        lblErroNome.setText(fields.contains("Nome") ? errors.get("Nome") : "");
        lblErroData.setText(fields.contains("Data") ? errors.get("Data") : "");
        lblErroSalario.setText(fields.contains("Salario") ? errors.get("Salario") : "");
        lblErroDepartamento.setText(fields.contains("Departamento") ? errors.get("Departamento") : "");
    }

}
