package com.locatehub.demo.model;

import java.util.ArrayList;
import java.util.List;

public class UserLocador extends User {
    // Atributos específicos
    private final List<String> meusProdutos;
    private double saldoDisponivel;

    public UserLocador(String id, String nome, String email) {
        super(id, nome, email);
        this.meusProdutos = new ArrayList<>();
    }

    // Implementação obrigatória do passo do Template Method
    @Override
    protected void carregarConfiguracoesEspecificas() {
        System.out.println("Carregando Painel de Vendas e Inventário do Locador.");
    }

    // Métodos específicos
    public void adicionarProduto(String produto) {
        this.meusProdutos.add(produto);
        System.out.println("Produto " + produto + " postado para aluguel.");
    }

    public void verificarGanhos() {
        System.out.println("Saldo atual para saque: R$ " + this.saldoDisponivel);
    }
}