package com.locatehub.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "ativo_imovel")
@DiscriminatorValue("IMOVEL")
@PrimaryKeyJoinColumn(name = "ativo_id")
public class AtivoImovel extends Ativo {

    @Column(name = "taxa_limpeza", nullable = false)
    private BigDecimal taxaLimpeza;

    public AtivoImovel() {
    }

    public AtivoImovel(Long id, String titulo, BigDecimal diaria, BigDecimal taxaLimpeza, Long donoId) {
        super(id, titulo, diaria, donoId);
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
