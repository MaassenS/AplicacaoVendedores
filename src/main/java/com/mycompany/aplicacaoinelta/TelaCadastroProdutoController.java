package com.mycompany.aplicacaoinelta;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.classes.Produto;
import model.exceptions.ValidacaoException;
import model.services.ProdutoService;

/**
 * FXML Controller class
 *
 * @author Sistema Inelta
 */
public class TelaCadastroProdutoController implements Initializable {

    private Produto produto;
    private List<String> listaLinhas;
    private ObservableList<String> listaComboLinhas;

    public void setProduto(Produto produto) {
        this.produto = produto;

        // Preenchendo os campos com os dados do produto
        txtId.setText(String.valueOf(produto.getId()));
        txtCodigo.setText(produto.getCodigo());
        txtNome.setText(produto.getNome());
        cmbLinha.getSelectionModel().select(produto.getLinha());
        txtCusto.setText(new DecimalFormat("0.00").format(produto.getPrecoCusto()));
        txtVenda.setText(new DecimalFormat("0.00").format(produto.getPrecoVenda()));
        txtPeso.setText(new DecimalFormat("0.000").format(produto.getPeso_kg()));
        chkAtivo.setSelected(produto.isAtivo());
    }

    @FXML
    private Label lblErroCodigo;
    @FXML
    private Label lblErroNome;
    @FXML
    private Label lblErroLinha;
    @FXML
    private Label lblErroCusto;
    @FXML
    private Label lblErroVenda;
    @FXML
    private Label lblErroPeso;

    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnSair;

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtNome;
    @FXML
    private ComboBox<String> cmbLinha;
    @FXML
    private TextField txtCusto;
    @FXML
    private TextField txtVenda;
    @FXML
    private TextField txtPeso;
    @FXML
    private CheckBox chkAtivo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializando lista de linhas de produtos
        listaLinhas = Arrays.asList("LINHA 471", "LINHA 485", "LINHA 490", "LINHA 472");
        listaComboLinhas = FXCollections.observableArrayList(listaLinhas);
        cmbLinha.setItems(listaComboLinhas);

        // Configurando formatação dos campos numéricos
        Utils.setTextFieldDouble(txtCusto);
        Utils.setTextFieldDouble(txtVenda);
        Utils.setTextFieldDouble(txtPeso);

        // Configurando evento do botão salvar
        btnSalvar.setOnAction((t) -> {
            try {
                ValidacaoException exc = new ValidacaoException("Erro de Validação!");

                // Se é um novo produto
                if (produto == null) {
                    produto = new Produto();
                }

                // Validação do código
                if (txtCodigo.getText() == null || txtCodigo.getText().trim().equals("")) {
                    exc.adicionarErro("Codigo", "O código não pode ser vazio!");
                } else if (txtCodigo.getText().trim().length() < 3) {
                    exc.adicionarErro("Codigo", "O código deve ter pelo menos 3 caracteres!");
                } else {
                    produto.setCodigo(txtCodigo.getText().trim().toUpperCase());
                }

                // Validação do nome
                if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
                    exc.adicionarErro("Nome", "O nome do produto não pode ser vazio!");
                } else if (txtNome.getText().trim().length() < 5) {
                    exc.adicionarErro("Nome", "O nome deve ter pelo menos 5 caracteres!");
                } else {
                    produto.setNome(txtNome.getText().trim());
                }

                // Validação da linha
                if (cmbLinha.getValue() == null) {
                    exc.adicionarErro("Linha", "Selecione uma linha de produto!");
                } else {
                    produto.setLinha(cmbLinha.getValue());
                }

                // Validação do preço de custo
                if (txtCusto.getText() == null || txtCusto.getText().trim().equals("")) {
                    exc.adicionarErro("Custo", "O preço de custo não pode ser vazio!");
                } else {
                    try {
                        double custo = Utils.tryParseToDouble(txtCusto.getText());
                        if (custo <= 0) {
                            exc.adicionarErro("Custo", "O preço de custo deve ser maior que zero!");
                        } else {
                            produto.setPrecoCusto(custo);
                        }
                    } catch (Exception e) {
                        exc.adicionarErro("Custo", "Informe um preço de custo válido!");
                    }
                }

                // Validação do preço de venda
                if (txtVenda.getText() == null || txtVenda.getText().trim().equals("")) {
                    exc.adicionarErro("Venda", "O preço de venda não pode ser vazio!");
                } else {
                    try {
                        double venda = Utils.tryParseToDouble(txtVenda.getText());
                        if (venda <= 0) {
                            exc.adicionarErro("Venda", "O preço de venda deve ser maior que zero!");
                        } else {
                            produto.setPrecoVenda(venda);

                            // Validação adicional: preço de venda deve ser maior que custo
                            if (produto.getPrecoCusto() > 0 && venda <= produto.getPrecoCusto()) {
                                exc.adicionarErro("Venda", "O preço de venda deve ser maior que o custo!");
                            }
                        }
                    } catch (Exception e) {
                        exc.adicionarErro("Venda", "Informe um preço de venda válido!");
                    }
                }

                // Validação do peso
                if (txtPeso.getText() == null || txtPeso.getText().trim().equals("")) {
                    exc.adicionarErro("Peso", "O peso não pode ser vazio!");
                } else {
                    try {
                        double peso = Utils.tryParseToDouble(txtPeso.getText());
                        if (peso <= 0) {
                            exc.adicionarErro("Peso", "O peso deve ser maior que zero!");
                        } else if (peso > 1000) {
                            exc.adicionarErro("Peso", "O peso não pode ser maior que 1000 kg!");
                        } else {
                            produto.setPeso_kg(peso);
                        }
                    } catch (Exception e) {
                        exc.adicionarErro("Peso", "Informe um peso válido!");
                    }
                }

                // Status ativo
                produto.setAtivo(chkAtivo.isSelected());

                // Se há erros de validação, lança a exceção
                if (!exc.getErrors().isEmpty()) {
                    throw exc;
                }

                // Tentativa de salvar no banco
                if (new ProdutoService().salvarOuAtualizar(produto)) {
                    Alert sucesso = new Alert(Alert.AlertType.INFORMATION);
                    sucesso.setTitle("Sucesso");
                    sucesso.setHeaderText(null);
                    sucesso.setContentText("Produto salvo com sucesso!");
                    sucesso.showAndWait();

                    // Fechando a tela
                    Stage stage = (Stage) btnSair.getScene().getWindow();
                    stage.close();
                } else {
                    Alert erro = new Alert(Alert.AlertType.ERROR);
                    erro.setTitle("ERRO");
                    erro.setHeaderText(null);
                    erro.setContentText("Ocorreu um erro ao salvar o produto!");
                    erro.showAndWait();
                }

            } catch (ValidacaoException ev) {
                setErrorMessages(ev.getErrors());
            } catch (Exception e) {
                Alert erro = new Alert(Alert.AlertType.ERROR);
                erro.setTitle("ERRO");
                erro.setHeaderText("Erro inesperado");
                erro.setContentText("Erro: " + e.getMessage());
                erro.showAndWait();
            }
        });

        // Configurando evento do botão sair
        btnSair.setOnAction((t) -> {
            ((Stage) btnSair.getScene().getWindow()).close();
        });
    }

    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        // Limpando todas as mensagens de erro
        lblErroCodigo.setText("");
        lblErroNome.setText("");
        lblErroLinha.setText("");
        lblErroCusto.setText("");
        lblErroVenda.setText("");
        lblErroPeso.setText("");

        // Definindo mensagens de erro para os campos que têm problemas
        lblErroCodigo.setText(fields.contains("Codigo") ? errors.get("Codigo") : "");
        lblErroNome.setText(fields.contains("Nome") ? errors.get("Nome") : "");
        lblErroLinha.setText(fields.contains("Linha") ? errors.get("Linha") : "");
        lblErroCusto.setText(fields.contains("Custo") ? errors.get("Custo") : "");
        lblErroVenda.setText(fields.contains("Venda") ? errors.get("Venda") : "");
        lblErroPeso.setText(fields.contains("Peso") ? errors.get("Peso") : "");
    }
}