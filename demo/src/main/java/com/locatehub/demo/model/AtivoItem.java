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

    @Column(name = "valor_caucao", nullable = false)
    private BigDecimal valorCaucao;

    public AtivoItem() {
    }

    public AtivoItem(Long id, String titulo, BigDecimal diaria, boolean caucao, BigDecimal valorCaucao, Long donoId) {
        super(id, titulo, diaria, donoId);
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
