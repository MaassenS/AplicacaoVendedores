package com.mycompany.aplicacaoinelta;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.classes.Produto;
import model.classes.Venda;
import model.services.ProdutoService;
import model.services.VendaService;

public class TelaDashboardController implements Initializable {

    // Controles de interface
    @FXML private ComboBox<String> cmbPeriodo;
    @FXML private Button btnAtualizar;
    @FXML private Button btnExportar;
    @FXML private Button btnVoltar;

    // Labels dos cards
    @FXML private Label lblReceitaTotal;
    @FXML private Label lblLucroTotal;
    @FXML private Label lblMargemMedia;
    @FXML private Label lblQuantidadeTotal;

    // Tabela e colunas
    @FXML private TableView<DashboardItem> tableViewTopProdutos;
    @FXML private TableColumn<DashboardItem, String> tableColumnCodigo;
    @FXML private TableColumn<DashboardItem, String> tableColumnNomeProduto;
    @FXML private TableColumn<DashboardItem, String> tableColumnLinha;
    @FXML private TableColumn<DashboardItem, Integer> tableColumnQtdLinha;
    @FXML private TableColumn<DashboardItem, Double> tableColumnReceita;
    @FXML private TableColumn<DashboardItem, Double> tableColumnLucro;
    @FXML private TableColumn<DashboardItem, Double> tableColumnMargem;

    // Services
    private ProdutoService produtoService = new ProdutoService();
    private VendaService vendaService = new VendaService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("=== INICIALIZANDO DASHBOARD ===");

        // Configurar ComboBox
        cmbPeriodo.getItems().addAll(
                "Janeiro 2025", "Fevereiro 2025", "Março 2025",
                "Abril 2025", "Maio 2025", "Junho 2025"
        );

        // Configurar colunas da tabela
        configurarColunas();

        // Carregar dados reais do banco
        carregarDadosReais();

        System.out.println("✅ Dashboard inicializado com sucesso!");
    }

    private void configurarColunas() {
        System.out.println("🔧 Configurando colunas da tabela...");

        // Configurar PropertyValueFactory para cada coluna
        tableColumnCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        tableColumnNomeProduto.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
        tableColumnLinha.setCellValueFactory(new PropertyValueFactory<>("linha"));
        tableColumnQtdLinha.setCellValueFactory(new PropertyValueFactory<>("quantidadeVendida"));
        tableColumnReceita.setCellValueFactory(new PropertyValueFactory<>("receita"));
        tableColumnLucro.setCellValueFactory(new PropertyValueFactory<>("lucro"));
        tableColumnMargem.setCellValueFactory(new PropertyValueFactory<>("margem"));

        System.out.println("✅ Colunas configuradas!");
    }

    private void carregarDadosReais() {
        System.out.println("📊 Carregando dados reais do banco...");

        try {
            // Carregar dados do banco (mesma lógica do teste)
            List<Produto> produtos = produtoService.getAll();
            List<Venda> vendas = vendaService.getAll();

            System.out.printf("📦 Dados carregados: %d produtos, %d vendas\n", produtos.size(), vendas.size());

            if (produtos.isEmpty() || vendas.isEmpty()) {
                System.out.println("❌ Não há dados para exibir no dashboard!");
                atualizarCardsVazios();
                return;
            }

            // Agrupar vendas por produto (mesma lógica do TesteDados)
            Map<Integer, DashboardData> dashboardMap = new HashMap<>();

            for (Venda venda : vendas) {
                // Buscar produto correspondente
                Produto produto = produtos.stream()
                        .filter(p -> p.getId() == venda.getProdutoId())
                        .findFirst()
                        .orElse(null);

                if (produto != null) {
                    // Obter ou criar entrada no dashboard
                    DashboardData data = dashboardMap.getOrDefault(venda.getProdutoId(),
                            new DashboardData(produto.getCodigo(), produto.getNome(), produto.getLinha()));

                    // Calcular valores para esta venda
                    double receitaVenda = produto.getPrecoVenda() * venda.getQuantidade();
                    double lucroVenda = produto.getLucroUnitario() * venda.getQuantidade();

                    // Acumular valores
                    data.quantidadeVendida += venda.getQuantidade();
                    data.receita += receitaVenda;
                    data.lucro += lucroVenda;

                    dashboardMap.put(venda.getProdutoId(), data);
                }
            }

            // Converter para lista e ordenar por quantidade vendida (Top 10)
            List<DashboardData> topProdutos = dashboardMap.values().stream()
                    .sorted((a, b) -> Integer.compare(b.quantidadeVendida, a.quantidadeVendida))
                    .limit(10)
                    .collect(Collectors.toList());

            // Calcular margem individual para cada produto
            topProdutos.forEach(data -> {
                data.margem = data.receita > 0 ? (data.lucro / data.receita) * 100 : 0;
            });

            // Converter para DashboardItem (classe para a tabela)
            List<DashboardItem> itensTabela = topProdutos.stream()
                    .map(data -> new DashboardItem(
                            data.codigo,
                            data.nome,
                            data.linha,
                            data.quantidadeVendida,
                            data.receita,
                            data.lucro,
                            data.margem
                    ))
                    .collect(Collectors.toList());

            // Aplicar à tabela
            ObservableList<DashboardItem> listaTabela = FXCollections.observableArrayList(itensTabela);
            tableViewTopProdutos.setItems(listaTabela);

            // Atualizar cards com totais
            atualizarCards(itensTabela);

            System.out.printf("✅ Dashboard carregado: %d produtos no top 10\n", itensTabela.size());

        } catch (Exception e) {
            System.err.println("❌ ERRO ao carregar dados do dashboard:");
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao carregar dados");
            alert.setContentText("Ocorreu um erro ao carregar os dados do dashboard: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void atualizarCards(List<DashboardItem> dados) {
        if (dados.isEmpty()) {
            atualizarCardsVazios();
            return;
        }

        double receitaTotal = dados.stream().mapToDouble(DashboardItem::getReceita).sum();
        double lucroTotal = dados.stream().mapToDouble(DashboardItem::getLucro).sum();
        int quantidadeTotal = dados.stream().mapToInt(DashboardItem::getQuantidadeVendida).sum();
        double margemMedia = receitaTotal > 0 ? (lucroTotal / receitaTotal) * 100 : 0;

        lblReceitaTotal.setText(String.format("R$ %.2f", receitaTotal));
        lblLucroTotal.setText(String.format("R$ %.2f", lucroTotal));
        lblQuantidadeTotal.setText(String.valueOf(quantidadeTotal));
        lblMargemMedia.setText(String.format("%.1f%%", margemMedia));

        System.out.printf("💰 Cards atualizados: Receita=R$%.2f, Lucro=R$%.2f, Qtd=%d, Margem=%.1f%%\n",
                receitaTotal, lucroTotal, quantidadeTotal, margemMedia);
    }

    private void atualizarCardsVazios() {
        lblReceitaTotal.setText("R$ 0,00");
        lblLucroTotal.setText("R$ 0,00");
        lblQuantidadeTotal.setText("0");
        lblMargemMedia.setText("0%");
    }

    @FXML
    public void onBtnAtualizarAction(ActionEvent event) {
        System.out.println("🔄 Atualizando dashboard...");
        carregarDadosReais();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dashboard");
        alert.setHeaderText(null);
        alert.setContentText("Dashboard atualizado com sucesso!");
        alert.showAndWait();
    }

    @FXML
    public void onBtnExportarAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exportar");
        alert.setHeaderText(null);
        alert.setContentText("Funcionalidade de exportação será implementada futuramente.");
        alert.showAndWait();
    }

    @FXML
    public void onBtnVoltarAction(ActionEvent event) {
        Stage stage = (Stage) btnVoltar.getScene().getWindow();
        stage.close();
    }

    /**
     * Classe auxiliar para cálculos internos
     */
    private static class DashboardData {
        String codigo;
        String nome;
        String linha;
        int quantidadeVendida = 0;
        double receita = 0.0;
        double lucro = 0.0;
        double margem = 0.0;

        public DashboardData(String codigo, String nome, String linha) {
            this.codigo = codigo;
            this.nome = nome;
            this.linha = linha;
        }
    }

    /**
     * Classe para itens da tabela JavaFX
     */
    public static class DashboardItem {
        private String codigo;
        private String nomeProduto;
        private String linha;
        private int quantidadeVendida;
        private double receita;
        private double lucro;
        private double margem;

        public DashboardItem(String codigo, String nomeProduto, String linha,
                             int quantidadeVendida, double receita, double lucro, double margem) {
            this.codigo = codigo;
            this.nomeProduto = nomeProduto;
            this.linha = linha;
            this.quantidadeVendida = quantidadeVendida;
            this.receita = receita;
            this.lucro = lucro;
            this.margem = margem;
        }

        // Getters para PropertyValueFactory
        public String getCodigo() { return codigo; }
        public String getNomeProduto() { return nomeProduto; }
        public String getLinha() { return linha; }
        public int getQuantidadeVendida() { return quantidadeVendida; }
        public double getReceita() { return receita; }
        public double getLucro() { return lucro; }
        public double getMargem() { return margem; }
    }
}
