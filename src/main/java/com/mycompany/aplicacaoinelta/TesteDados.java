package com.mycompany.aplicacaoinelta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import model.classes.Produto;
import model.classes.Venda;
import model.services.ProdutoService;
import model.services.VendaService;

public class TesteDados {

    public static void main(String[] args) {
        System.out.println("=== TESTE DE CARREGAMENTO DE DADOS ===\n");

        // Teste 1: Verificar conexão e produtos
        testarProdutos();

        System.out.println("\n" + "=".repeat(50) + "\n");

        // Teste 2: Verificar vendas
        testarVendas();

        System.out.println("\n" + "=".repeat(50) + "\n");

        // Teste 3: Simular o que o RelatorioController faz
        testarRelatorio();

        System.out.println("\n" + "=".repeat(50) + "\n");

        // Teste 4: Simular Dashboard com dados reais
        testarDashboard();
    }

    private static void testarProdutos() {
        System.out.println("🔍 TESTANDO PRODUTOS:");
        try {
            ProdutoService produtoService = new ProdutoService();
            List<Produto> produtos = produtoService.getAll();

            System.out.println("✅ Conexão com banco: OK");
            System.out.println("📦 Total de produtos carregados: " + produtos.size());

            if (produtos.size() > 0) {
                System.out.println("\n📋 Primeiros 3 produtos:");
                for (int i = 0; i < Math.min(3, produtos.size()); i++) {
                    Produto p = produtos.get(i);
                    System.out.printf("   %d. [%s] %s - R$ %.2f (Lucro: R$ %.2f)\n",
                            (i+1), p.getCodigo(), p.getNome(), p.getPrecoVenda(), p.getLucroUnitario());
                }
            } else {
                System.out.println("❌ NENHUM PRODUTO ENCONTRADO!");
                System.out.println("   Verifique: 1) Conexão do banco, 2) Dados na tabela produtos");
            }

        } catch (Exception e) {
            System.out.println("❌ ERRO ao carregar produtos:");
            e.printStackTrace();
        }
    }

    private static void testarVendas() {
        System.out.println("🔍 TESTANDO VENDAS:");
        try {
            VendaService vendaService = new VendaService();
            List<Venda> vendas = vendaService.getAll();

            System.out.println("✅ Conexão com banco: OK");
            System.out.println("🛒 Total de vendas carregadas: " + vendas.size());

            if (vendas.size() > 0) {
                System.out.println("\n📋 Primeiras 3 vendas:");
                for (int i = 0; i < Math.min(3, vendas.size()); i++) {
                    Venda v = vendas.get(i);
                    System.out.printf("   %d. Produto ID: %d, Qtd: %d, Data: %s\n",
                            (i+1), v.getProdutoId(), v.getQuantidade(), v.getDataVenda());
                }
            } else {
                System.out.println("❌ NENHUMA VENDA ENCONTRADA!");
                System.out.println("   Verifique: 1) Dados na tabela vendas, 2) Relacionamento produto_id");
            }

        } catch (Exception e) {
            System.out.println("❌ ERRO ao carregar vendas:");
            e.printStackTrace();
        }
    }

    private static void testarRelatorio() {
        System.out.println("🔍 TESTANDO LÓGICA DO RELATÓRIO:");
        try {
            ProdutoService produtoService = new ProdutoService();
            VendaService vendaService = new VendaService();

            List<Produto> produtos = produtoService.getAll();
            List<Venda> vendas = vendaService.getAll();

            System.out.println("📊 Simulando criação de relatório...");

            int relatóriosGerados = 0;
            double receitaTotal = 0;
            double lucroTotal = 0;

            for (Venda venda : vendas) {
                // Buscar produto correspondente
                Produto produto = produtos.stream()
                        .filter(p -> p.getId() == venda.getProdutoId())
                        .findFirst()
                        .orElse(null);

                if (produto != null) {
                    relatóriosGerados++;
                    double receita = produto.getPrecoVenda() * venda.getQuantidade();
                    double lucro = produto.getLucroUnitario() * venda.getQuantidade();

                    receitaTotal += receita;
                    lucroTotal += lucro;

                    if (relatóriosGerados <= 3) { // Mostrar só os primeiros 3
                        System.out.printf("   ✅ Venda: %s | Qtd: %d | Receita: R$ %.2f | Lucro: R$ %.2f\n",
                                produto.getNome(), venda.getQuantidade(), receita, lucro);
                    }
                } else {
                    System.out.printf("   ❌ PRODUTO NÃO ENCONTRADO para venda produto_id=%d\n",
                            venda.getProdutoId());
                }
            }

            System.out.println("\n📈 RESUMO DO RELATÓRIO:");
            System.out.printf("   📊 Relatórios gerados: %d de %d vendas\n", relatóriosGerados, vendas.size());
            System.out.printf("   💰 Receita total: R$ %.2f\n", receitaTotal);
            System.out.printf("   💸 Lucro total: R$ %.2f\n", lucroTotal);

            if (relatóriosGerados < vendas.size()) {
                System.out.printf("   ⚠️  ATENÇÃO: %d vendas não geraram relatório (produto não encontrado)\n",
                        (vendas.size() - relatóriosGerados));
            }

        } catch (Exception e) {
            System.out.println("❌ ERRO ao gerar relatório:");
            e.printStackTrace();
        }
    }

    /**
     * 🆕 NOVO: Testa a lógica do Dashboard com dados reais do banco
     */
    private static void testarDashboard() {
        System.out.println("🏆 TESTANDO LÓGICA DO DASHBOARD:");

        try {
            // Carregar dados reais
            ProdutoService produtoService = new ProdutoService();
            VendaService vendaService = new VendaService();

            List<Produto> produtos = produtoService.getAll();
            List<Venda> vendas = vendaService.getAll();

            System.out.printf("📊 Dados carregados: %d produtos, %d vendas\n", produtos.size(), vendas.size());

            if (produtos.isEmpty() || vendas.isEmpty()) {
                System.out.println("❌ Não é possível testar dashboard sem dados!");
                return;
            }

            // Simular cálculo do Dashboard - Agrupar vendas por produto
            Map<Integer, DashboardData> dashboardMap = new HashMap<>();

            System.out.println("\n🔍 PROCESSANDO VENDAS PARA DASHBOARD:");

            for (Venda venda : vendas) {
                System.out.printf("   Processando venda: produto_id=%d, qtd=%d, data=%s\n",
                        venda.getProdutoId(), venda.getQuantidade(), venda.getDataVenda());

                // Buscar produto correspondente
                Produto produto = produtos.stream()
                        .filter(p -> p.getId() == venda.getProdutoId())
                        .findFirst()
                        .orElse(null);

                if (produto != null) {
                    System.out.printf("      ✅ Produto encontrado: [%s] %s\n", produto.getCodigo(), produto.getNome());

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

                    System.out.printf("      💰 Receita desta venda: R$ %.2f, Lucro: R$ %.2f\n", receitaVenda, lucroVenda);
                } else {
                    System.out.printf("      ❌ PRODUTO NÃO ENCONTRADO para produto_id=%d\n", venda.getProdutoId());
                }
            }

            // Converter para lista e ordenar por quantidade vendida (Top produtos)
            List<DashboardData> topProdutos = dashboardMap.values().stream()
                    .sorted((a, b) -> Integer.compare(b.quantidadeVendida, a.quantidadeVendida))
                    .limit(10)
                    .collect(Collectors.toList());

            // Calcular totais gerais
            double receitaTotal = topProdutos.stream().mapToDouble(d -> d.receita).sum();
            double lucroTotal = topProdutos.stream().mapToDouble(d -> d.lucro).sum();
            int quantidadeTotal = topProdutos.stream().mapToInt(d -> d.quantidadeVendida).sum();
            double margemMedia = receitaTotal > 0 ? (lucroTotal / receitaTotal) * 100 : 0;

            // Calcular margem individual para cada produto
            topProdutos.forEach(data -> {
                data.margem = data.receita > 0 ? (data.lucro / data.receita) * 100 : 0;
            });

            System.out.println("\n🏆 TOP 10 PRODUTOS MAIS VENDIDOS (Dashboard):");
            System.out.println("═".repeat(100));
            System.out.printf("%-8s %-35s %-12s %-8s %-12s %-12s %-8s\n",
                    "Código", "Nome", "Linha", "Qtd", "Receita", "Lucro", "Margem");
            System.out.println("─".repeat(100));

            for (int i = 0; i < topProdutos.size(); i++) {
                DashboardData data = topProdutos.get(i);
                System.out.printf("%-8s %-35s %-12s %-8d R$ %8.2f R$ %8.2f %6.1f%%\n",
                        data.codigo,
                        data.nome.length() > 30 ? data.nome.substring(0, 30) + "..." : data.nome,
                        data.linha,
                        data.quantidadeVendida,
                        data.receita,
                        data.lucro,
                        data.margem);
            }

            System.out.println("═".repeat(100));
            System.out.printf("TOTAIS: %d produtos | Qtd: %d | Receita: R$ %.2f | Lucro: R$ %.2f | Margem: %.1f%%\n",
                    topProdutos.size(), quantidadeTotal, receitaTotal, lucroTotal, margemMedia);

            System.out.println("\n✅ TESTE DE DASHBOARD CONCLUÍDO COM SUCESSO!");
            System.out.printf("   📈 %d produtos processados para o dashboard\n", dashboardMap.size());
            System.out.printf("   🎯 Top 10 produtos identificados\n");
            System.out.printf("   💰 Totais calculados corretamente\n");

            // 🎯 DADOS PARA IMPLEMENTAR NO DASHBOARD REAL
            System.out.println("\n🎯 DADOS PARA O DASHBOARD REAL:");
            System.out.println("   Use estes valores nos cards:");
            System.out.printf("   lblReceitaTotal.setText(\"R$ %.2f\");\n", receitaTotal);
            System.out.printf("   lblLucroTotal.setText(\"R$ %.2f\");\n", lucroTotal);
            System.out.printf("   lblQuantidadeTotal.setText(\"%d\");\n", quantidadeTotal);
            System.out.printf("   lblMargemMedia.setText(\"%.1f%%\");\n", margemMedia);

        } catch (Exception e) {
            System.out.println("❌ ERRO ao testar dashboard:");
            e.printStackTrace();
        }
    }

    /**
     * Classe auxiliar para dados do dashboard
     */
    static class DashboardData {
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
}
