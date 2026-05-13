package com.locatehub.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

@Entity
public class UserLocador extends User {
    // Atributos específicos
    @Transient
    private final List<String> meusProdutos;
    private double saldoDisponivel;

    public UserLocador() {
        this.meusProdutos = new ArrayList<>();
    }

    public List<String> getMeusProdutos() {
        return meusProdutos;
    }

    public double getSaldoDisponivel() {
        return saldoDisponivel;
    }

    public void setSaldoDisponivel(double saldoDisponivel) {
        this.saldoDisponivel = saldoDisponivel;
    }

    // Implementação do passo do Template Method
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