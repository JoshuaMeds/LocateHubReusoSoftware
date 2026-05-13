package com.locatehub.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "ativo_automovel")
@DiscriminatorValue("AUTOMOVEL")
@PrimaryKeyJoinColumn(name = "ativo_id")
public class AtivoAutomovel extends Ativo {

    @Column(name = "seguro_diario")
    private BigDecimal seguroDiario;

    public AtivoAutomovel() {
    }

    public AtivoAutomovel(String titulo, BigDecimal diaria, Long donoId, String descricao) {
        super(titulo, diaria, donoId, descricao);
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
