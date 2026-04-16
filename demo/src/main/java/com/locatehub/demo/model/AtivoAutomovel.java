package com.locatehub.demo.model;

import java.math.BigDecimal;

public class Automovel extends Bem {

    private BigDecimal seguroDiario;

    public Automovel(Long id, String titulo, BigDecimal diaria, BigDecimal seguroDiario) {
        super(id, titulo, diaria);
        this.seguroDiario = seguroDiario;
    }

    @Override
    protected BigDecimal calcularAdicional(int dias) {
        return seguroDiario.multiply(BigDecimal.valueOf(dias));
    }

    @Override
    protected BigDecimal calcularDesconto(int dias, BigDecimal base) {
        return dias >= 10 ? base.multiply(new BigDecimal("0.12")) : BigDecimal.ZERO;
    }

    @Override
    public String getTipo() {
        return "Automóvel";
    }
}