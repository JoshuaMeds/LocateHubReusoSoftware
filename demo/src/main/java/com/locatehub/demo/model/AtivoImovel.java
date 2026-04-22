package com.locatehub.demo.model;

import java.math.BigDecimal;

public class AtivoImovel extends Ativo {

    private BigDecimal taxaLimpeza;

    public AtivoImovel(Long id, String titulo, BigDecimal diaria, BigDecimal taxaLimpeza) {
        super(id, titulo, diaria);
        this.taxaLimpeza = taxaLimpeza;
    }

    @Override
    protected BigDecimal calcularAdicional(int dias) {
        return taxaLimpeza;
    }

    @Override
    protected BigDecimal calcularDesconto(int dias, BigDecimal base) {
        return dias >= 5 ? base.multiply(new BigDecimal("0.08")) : BigDecimal.ZERO;
    }

    @Override
    public String getTipo() {
        return "Imóvel";
    }
}