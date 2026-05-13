package com.locatehub.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "ativo")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
public abstract class Ativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "valor_diaria", nullable = false)
    private BigDecimal valorDiaria;

    private boolean disponivel = true;

    @Column(name = "dono_id", nullable = false)
    private Long donoId;

    protected Ativo() {
    }

    public Ativo(Long id, String titulo, BigDecimal valorDiaria, Long donoId) {
        this.id = id;
        this.titulo = titulo;
        this.valorDiaria = valorDiaria;
        this.donoId = donoId;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
    this.descricao = descricao;
}

    public BigDecimal getValorDiaria() {
        return valorDiaria;
    }

    public Long getDonoId() {
        return donoId;
    }

    public boolean podeSerGerenciadoPor(Long usuarioId) {
        return donoId != null && donoId.equals(usuarioId);
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
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
