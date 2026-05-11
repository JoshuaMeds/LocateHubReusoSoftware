package com.locatehub.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "lista_desejos",
        uniqueConstraints = @UniqueConstraint(columnNames = {"locatario_id", "ativo_id"})
)
public class ListaDesejos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "locatario_id", nullable = false)
    private UserLocatario locatario;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ativo_id", nullable = false)
    private Ativo ativo;

    protected ListaDesejos() {
    }

    public ListaDesejos(UserLocatario locatario, Ativo ativo) {
        this.locatario = locatario;
        this.ativo = ativo;
    }

    public Ativo getAtivo() {
        return ativo;
    }
}
