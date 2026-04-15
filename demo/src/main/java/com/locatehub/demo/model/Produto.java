package com.locatehub.demo.model;

public abstract class Produto {
    private String nome;
    private Double valor;

    public Produto(String nome, Double valor) {
        this.nome = nome;
        this.valor = valor;
    }
}
