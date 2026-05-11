package com.locatehub.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ativo_id")
    private Ativo ativo;

    @ManyToOne
    @JoinColumn(name = "locatario_id")
    private UserLocatario locatario;

    private LocalDate dataInicio;

    private LocalDate dataFim;

    private BigDecimal valorTotal;

    private boolean cancelada = false;

    protected Reserva() {
    }

    public Reserva(Ativo ativo, UserLocatario locatario, LocalDate dataInicio, LocalDate dataFim, BigDecimal valorTotal) {
        this.ativo = ativo;
        this.locatario = locatario;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valorTotal = valorTotal;
    }

    public Long getId() {
        return id;
    }

    public Ativo getAtivo() {
        return ativo;
    }

    public UserLocatario getLocatario() {
        return locatario;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public boolean isCancelada() {
        return cancelada;
    }

    public boolean podeCancelar(LocalDate hoje) {
        return !cancelada && hoje.isBefore(dataInicio);
    }

    public void cancelar() {
        this.cancelada = true;
    }
}
