package com.locatehub.demo.dashboard;

public class DashboardLocatario extends DashboardFinanceiroTemplate {

    @Override
    protected void exibirResumoFinanceiro() {
        System.out.println("--- Resumo Financeiro (Visão Locatário) ---");
        System.out.println("Foco: Controle de Despesas");
        System.out.println("-> Histórico de pagamentos realizados");
        System.out.println("-> Total gasto no mês: R$ 450,00");
    }

    @Override
    protected void exibirConfiguracoesPagamento() {
        System.out.println("--- Métodos de Pagamento ---");
        System.out.println("-> Cartões salvos: **** **** **** 1234");
        System.out.println("-> Métodos utilizados recentemente: Cartão de Crédito e PIX");
    }
}