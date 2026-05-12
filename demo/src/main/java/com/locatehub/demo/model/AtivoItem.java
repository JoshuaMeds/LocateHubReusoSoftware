package com.locatehub.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "ativo_item")
@DiscriminatorValue("ITEM")
@PrimaryKeyJoinColumn(name = "ativo_id")
public class AtivoItem extends Ativo {

    private boolean caucao;

    @Column(name = "valor_caucao")
    private BigDecimal valorCaucao;

    public AtivoItem() {
    }

    public AtivoItem(String titulo, BigDecimal diaria, Long donoId, String descricao) {
        super(titulo, diaria, donoId,descricao);
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
