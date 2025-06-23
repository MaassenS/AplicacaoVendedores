package com.mycompany.aplicacaoinelta;

import java.io.IOException;
import java.net.URL;
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
import model.classes.Produto;
import model.services.ProdutoService;

/**
 * FXML Controller class
 *
 * @author Sistema Inelta
 */
public class TelaProdutosController implements Initializable {

    @FXML
    private Button btnNovo;
    @FXML
    private Button btnExcluir;
    @FXML
    private Button btnDashboard;
    @FXML
    private Button btnRelatorios;

    @FXML
    private TableView<Produto> tableViewProdutos;
    @FXML
    private TableColumn<Produto, Integer> tableColumnId;
    @FXML
    private TableColumn<Produto, String> tableColumnCodigo;
    @FXML
    private TableColumn<Produto, String> tableColumnNome;
    @FXML
    private TableColumn<Produto, String> tableColumnLinha;
    @FXML
    private TableColumn<Produto, Double> tableColumnCusto;
    @FXML
    private TableColumn<Produto, Double> tableColumnVenda;
    @FXML
    private TableColumn<Produto, Double> tableColumnLucro;
    @FXML
    private TableColumn<Produto, Double> tableColumnMargem;
    @FXML
    private TableColumn<Produto, Double> tableColumnPeso;
    @FXML
    private TableColumn<Produto, String> tableColumnAtivo;

    private ObservableList<Produto> listaProdutos;

    @FXML
    public void onBtnNovoAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TelaCadastroProduto.fxml"));
            Pane pane = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Cadastro de Produto - Sistema Inelta");
            stage.setScene(new Scene(pane));
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
        if (tableViewProdutos.getSelectionModel().getSelectedItem() != null) {
            Produto produto = tableViewProdutos.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação");
            alert.setHeaderText(null);
            alert.setContentText("O produto \"" + produto.getNome() + "\" será excluído! Tem certeza?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                if (new ProdutoService().excluir(produto)) {
                    Alert mensagem = new Alert(Alert.AlertType.INFORMATION);
                    mensagem.setTitle("Excluído");
                    mensagem.setHeaderText(null);
                    mensagem.setContentText("Produto excluído com sucesso!");
                    mensagem.showAndWait();
                    atualizarTabela();
                } else {
                    Alert erro = new Alert(Alert.AlertType.ERROR);
                    erro.setTitle("Erro");
                    erro.setHeaderText(null);
                    erro.setContentText("Erro ao excluir o produto. Verifique se não há vendas associadas.");
                    erro.showAndWait();
                }
            }
        } else {
            Alert aviso = new Alert(Alert.AlertType.WARNING);
            aviso.setTitle("Atenção");
            aviso.setHeaderText(null);
            aviso.setContentText("Selecione um produto para excluir.");
            aviso.showAndWait();
        }
    }

    @FXML
    public void onBtnDashboard(ActionEvent event) {
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
    }

    @FXML
    public void onBtnRelatorios(ActionEvent event) {
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
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurando as colunas da tabela
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnLinha.setCellValueFactory(new PropertyValueFactory<>("linha"));

        // Configurando colunas monetárias com formatação
        tableColumnCusto.setCellValueFactory(new PropertyValueFactory<>("precoCusto"));
        Utils.formatTableColumnDouble(tableColumnCusto, 2);

        tableColumnVenda.setCellValueFactory(new PropertyValueFactory<>("precoVenda"));
        Utils.formatTableColumnDouble(tableColumnVenda, 2);

        tableColumnLucro.setCellValueFactory(new PropertyValueFactory<>("lucroUnitario"));
        Utils.formatTableColumnDouble(tableColumnLucro, 2);

        tableColumnMargem.setCellValueFactory(new PropertyValueFactory<>("margemLucro"));
        Utils.formatTableColumnDouble(tableColumnMargem, 2);

        tableColumnPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        Utils.formatTableColumnDouble(tableColumnPeso, 3);

        tableColumnAtivo.setCellValueFactory(new PropertyValueFactory<>("ativoTexto"));

        // Carregando dados iniciais
        atualizarTabela();

        // Implementando evento de duplo clique para edição
        tableViewProdutos.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2 && tableViewProdutos.getSelectionModel().getSelectedItem() != null) {
                    Produto produto = tableViewProdutos.getSelectionModel().getSelectedItem();
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("TelaCadastroProduto.fxml"));
                        Pane pane = loader.load();

                        Stage stage = new Stage();
                        stage.setTitle("Editar Produto - Sistema Inelta");
                        stage.setScene(new Scene(pane));
                        stage.initOwner(btnNovo.getScene().getWindow());
                        stage.initModality(Modality.WINDOW_MODAL);

                        TelaCadastroProdutoController controller = loader.getController();
                        controller.setProduto(produto);

                        stage.showAndWait();
                        atualizarTabela();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    public void atualizarTabela() {
        try {
            // Carregando produtos do banco de dados
            listaProdutos = FXCollections.observableArrayList(new ProdutoService().getAll());
            tableViewProdutos.setItems(listaProdutos);
        } catch (Exception e) {
            Alert erro = new Alert(Alert.AlertType.ERROR);
            erro.setTitle("Erro");
            erro.setHeaderText("Erro ao carregar produtos");
            erro.setContentText("Não foi possível carregar a lista de produtos: " + e.getMessage());
            erro.showAndWait();
        }
    }
}