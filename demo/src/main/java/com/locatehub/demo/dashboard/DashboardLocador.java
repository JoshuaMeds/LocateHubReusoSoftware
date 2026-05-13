package com.locatehub.demo.dashboard;

public class DashboardLocador extends DashboardFinanceiroTemplate {

    @Override
    protected void exibirResumoFinanceiro() {
        System.out.println("--- Resumo Financeiro (Visão Locador) ---");
        System.out.println("Foco: Controle de Receitas");
        System.out.println("-> Extrato de ganhos acumulados: R$ 1.250,00");
        System.out.println("-> Projeções de recebimentos futuros: R$ 300,00");
        System.out.println("-> [Renderizando Gráfico de Desempenho...]");
    }

    @Override
    protected void exibirConfiguracoesPagamento() {
        System.out.println("--- Configurações de Recebimento ---");
        System.out.println("-> Chaves PIX para repasse: email@locatehub.com.br");
        System.out.println("-> Status do repasse automático: Ativo");
    }
}