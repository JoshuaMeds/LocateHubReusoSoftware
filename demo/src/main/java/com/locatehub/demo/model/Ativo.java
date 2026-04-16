package com.locatehub.demo.model;

import java.math.BigDecimal;

public abstract class Ativo {

    private Long id;
    private String titulo;
    private BigDecimal valorDiaria;
    private boolean disponivel = true;

    public Ativo(Long id, String titulo, BigDecimal valorDiaria) {
        this.id = id;
        this.titulo = titulo;
        this.valorDiaria = valorDiaria;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void indisponibilizar() {
        this.disponivel = false;
    }

    public BigDecimal calcularValorLocacao(int dias) {
        if (!disponivel) throw new IllegalStateException("Indisponível");
        if (dias <= 0) throw new IllegalArgumentException("Dias inválidos");

        BigDecimal base = valorDiaria.multiply(BigDecimal.valueOf(dias));
        return base.add(calcularAdicional(dias)).subtract(calcularDesconto(dias, base));
    }

    protected BigDecimal calcularDesconto(int dias, BigDecimal base) {
        return BigDecimal.ZERO;
    }

    protected abstract BigDecimal calcularAdicional(int dias);

    public abstract String getTipo();
}