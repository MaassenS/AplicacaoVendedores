/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.aplicacaoinelta;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.classes.Produto;
import model.classes.Venda;
import model.services.ProdutoService;
import model.services.VendaService;

/**
 * FXML Controller class
 *
 * @author Sistema Inelta
 */
public class TelaRelatoriosController implements Initializable {

    // Controles de filtro
    @FXML
    private DatePicker dateInicio;
    @FXML
    private DatePicker dateFim;
    @FXML
    private ComboBox<String> cmbLinha;
    @FXML
    private TextField txtBusca;
    @FXML
    private Button btnFiltrar;
    @FXML
    private Button btnLimpar;
    @FXML
    private Button btnExportar;
    @FXML
    private Button btnVoltar;

    // Labels de totalizadores
    @FXML
    private Label lblTotalVendas;
    @FXML
    private Label lblTotalReceita;
    @FXML
    private Label lblTotalLucro;
    @FXML
    private Label lblMargemGeral;

    // Tabela de relatório
    @FXML
    private TableView<RelatorioVenda> tableViewRelatorio;
    @FXML
    private TableColumn<RelatorioVenda, LocalDate> tableColumnData;
    @FXML
    private TableColumn<RelatorioVenda, String> tableColumnProduto;
    @FXML
    private TableColumn<RelatorioVenda, String> tableColumnLinha;
    @FXML
    private TableColumn<RelatorioVenda, Integer> tableColumnQuantidade;
    @FXML
    private TableColumn<RelatorioVenda, Double> tableColumnPrecoUnitario;
    @FXML
    private TableColumn<RelatorioVenda, Double> tableColumnReceita;
    @FXML
    private TableColumn<RelatorioVenda, Double> tableColumnLucroUnitario;
    @FXML
    private TableColumn<RelatorioVenda, Double> tableColumnLucroTotal;
    @FXML
    private TableColumn<RelatorioVenda, Double> tableColumnMargem;

    private ObservableList<RelatorioVenda> listaRelatorio;

    // Services
    private ProdutoService produtoService = new ProdutoService();
    private VendaService vendaService = new VendaService();

    // Lista completa para filtros
    private List<RelatorioVenda> relatorioCompleto;

    @FXML
    public void onBtnFiltrarAction(ActionEvent event) {
        try {
            aplicarFiltros();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao aplicar filtros: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void onBtnLimparAction(ActionEvent event) {
        // Limpar todos os filtros
        dateInicio.setValue(null);
        dateFim.setValue(null);
        cmbLinha.setValue("Todas as linhas");
        txtBusca.clear();

        // Mostrar todos os dados novamente
        listaRelatorio = FXCollections.observableArrayList(relatorioCompleto);
        tableViewRelatorio.setItems(listaRelatorio);

        // Recalcular totalizadores
        calcularTotalizadores(listaRelatorio);
    }

    @FXML
    public void onBtnExportarAction(ActionEvent event) {
        try {
            // Implementação futura de exportação
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Exportar");
            alert.setHeaderText(null);
            alert.setContentText("Funcionalidade de exportação será implementada em versão futura.");
            alert.showAndWait();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao exportar: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void onBtnVoltarAction(ActionEvent event) {
        Stage stage = (Stage) btnVoltar.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurando colunas da tabela
        tableColumnData.setCellValueFactory(new PropertyValueFactory<>("dataVenda"));
        Utils.formatTableColumnDate(tableColumnData);
        tableColumnProduto.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
        tableColumnLinha.setCellValueFactory(new PropertyValueFactory<>("linha"));
        tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        tableColumnPrecoUnitario.setCellValueFactory(new PropertyValueFactory<>("precoUnitario"));
        Utils.formatTableColumnDouble(tableColumnPrecoUnitario, 2);
        tableColumnReceita.setCellValueFactory(new PropertyValueFactory<>("receita"));
        Utils.formatTableColumnDouble(tableColumnReceita, 2);
        tableColumnLucroUnitario.setCellValueFactory(new PropertyValueFactory<>("lucroUnitario"));
        Utils.formatTableColumnDouble(tableColumnLucroUnitario, 2);
        tableColumnLucroTotal.setCellValueFactory(new PropertyValueFactory<>("lucroTotal"));
        Utils.formatTableColumnDouble(tableColumnLucroTotal, 2);
        tableColumnMargem.setCellValueFactory(new PropertyValueFactory<>("margem"));
        Utils.formatTableColumnDouble(tableColumnMargem, 1);

        // Inicializando ComboBox de linhas
        cmbLinha.getItems().addAll(
                "Todas as linhas",
                "LINHA 471",
                "LINHA 485",
                "LINHA 490",
                "LINHA 472"
        );
        cmbLinha.setValue("Todas as linhas");

        // Carregando dados iniciais
        carregarDados();
    }

    private void carregarDados() {
        try {
            List<Produto> produtos = produtoService.getAll();
            List<Venda> vendas = vendaService.getAll();

            relatorioCompleto = new ArrayList<>();

            for (Venda venda : vendas) {
                Produto produto = produtos.stream()
                        .filter(p -> p.getId() == venda.getProdutoId())
                        .findFirst()
                        .orElse(null);

                if (produto != null) {
                    double receita = produto.getPrecoVenda() * venda.getQuantidade();
                    double lucroTotal = produto.getLucroUnitario() * venda.getQuantidade();
                    double margem = produto.getMargemLucro();

                    relatorioCompleto.add(new RelatorioVenda(
                            venda.getDataVenda(),
                            produto.getNome(),
                            produto.getLinha(),
                            venda.getQuantidade(),
                            produto.getPrecoVenda(),
                            receita,
                            produto.getLucroUnitario(),
                            lucroTotal,
                            margem
                    ));
                }
            }

            // Ordenar por data (mais recente primeiro)
            relatorioCompleto.sort((r1, r2) -> r2.getDataVenda().compareTo(r1.getDataVenda()));

            listaRelatorio = FXCollections.observableArrayList(relatorioCompleto);
            tableViewRelatorio.setItems(listaRelatorio);

            // Calcular totalizadores
            calcularTotalizadores(listaRelatorio);

        } catch (Exception e) {
            System.err.println("Erro ao carregar dados: " + e.getMessage());
        }
    }

    private void aplicarFiltros() {
        List<RelatorioVenda> dadosFiltrados = new ArrayList<>(relatorioCompleto);

        // Filtro por data de início
        if (dateInicio.getValue() != null) {
            dadosFiltrados = dadosFiltrados.stream()
                    .filter(r -> !r.getDataVenda().isBefore(dateInicio.getValue()))
                    .collect(Collectors.toList());
        }

        // Filtro por data de fim
        if (dateFim.getValue() != null) {
            dadosFiltrados = dadosFiltrados.stream()
                    .filter(r -> !r.getDataVenda().isAfter(dateFim.getValue()))
                    .collect(Collectors.toList());
        }

        // Filtro por linha
        if (cmbLinha.getValue() != null && !cmbLinha.getValue().equals("Todas as linhas")) {
            dadosFiltrados = dadosFiltrados.stream()
                    .filter(r -> r.getLinha().equals(cmbLinha.getValue()))
                    .collect(Collectors.toList());
        }

        // Filtro por busca textual (nome do produto)
        if (txtBusca.getText() != null && !txtBusca.getText().trim().isEmpty()) {
            String termo = txtBusca.getText().toLowerCase();
            dadosFiltrados = dadosFiltrados.stream()
                    .filter(r -> r.getNomeProduto().toLowerCase().contains(termo))
                    .collect(Collectors.toList());
        }

        listaRelatorio = FXCollections.observableArrayList(dadosFiltrados);
        tableViewRelatorio.setItems(listaRelatorio);

        // Recalcular totalizadores com dados filtrados
        calcularTotalizadores(listaRelatorio);
    }

    private void calcularTotalizadores(List<RelatorioVenda> dados) {
        int totalVendas = dados.size();
        double totalReceita = dados.stream().mapToDouble(RelatorioVenda::getReceita).sum();
        double totalLucro = dados.stream().mapToDouble(RelatorioVenda::getLucroTotal).sum();
        double margemGeral = totalReceita > 0 ? (totalLucro / totalReceita) * 100 : 0;

        lblTotalVendas.setText(String.valueOf(totalVendas));
        lblTotalReceita.setText(String.format("R$ %.2f", totalReceita));
        lblTotalLucro.setText(String.format("R$ %.2f", totalLucro));
        lblMargemGeral.setText(String.format("%.1f%%", margemGeral));
    }

    // Classe auxiliar para o relatório
    public static class RelatorioVenda {
        private LocalDate dataVenda;
        private String nomeProduto;
        private String linha;
        private int quantidade;
        private double precoUnitario;
        private double receita;
        private double lucroUnitario;
        private double lucroTotal;
        private double margem;

        public RelatorioVenda(LocalDate dataVenda, String nomeProduto, String linha,
                              int quantidade, double precoUnitario, double receita,
                              double lucroUnitario, double lucroTotal, double margem) {
            this.dataVenda = dataVenda;
            this.nomeProduto = nomeProduto;
            this.linha = linha;
            this.quantidade = quantidade;
            this.precoUnitario = precoUnitario;
            this.receita = receita;
            this.lucroUnitario = lucroUnitario;
            this.lucroTotal = lucroTotal;
            this.margem = margem;
        }

        // Getters
        public LocalDate getDataVenda() { return dataVenda; }
        public String getNomeProduto() { return nomeProduto; }
        public String getLinha() { return linha; }
        public int getQuantidade() { return quantidade; }
        public double getPrecoUnitario() { return precoUnitario; }
        public double getReceita() { return receita; }
        public double getLucroUnitario() { return lucroUnitario; }
        public double getLucroTotal() { return lucroTotal; }
        public double getMargem() { return margem; }
    }
}