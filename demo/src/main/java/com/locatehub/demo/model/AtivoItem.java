package com.locatehub.demo.model;

import java.math.BigDecimal;

public class AtivoItem extends Ativo {

    private boolean caucao;
    private BigDecimal valorCaucao;

    public AtivoItem(Long id, String titulo, BigDecimal diaria, boolean caucao, BigDecimal valorCaucao) {
        super(id, titulo, diaria);
        this.caucao = caucao;
        this.valorCaucao = valorCaucao;
    }

    @Override
    protected BigDecimal calcularAdicional(int dias) {
        return caucao ? valorCaucao : BigDecimal.ZERO;
    }

    @Override
    protected BigDecimal calcularDesconto(int dias, BigDecimal base) {
        return dias >= 7 ? base.multiply(new BigDecimal("0.1")) : BigDecimal.ZERO;
    }

    @Override
    public String getTipo() {
        return "Item";
    }
}
