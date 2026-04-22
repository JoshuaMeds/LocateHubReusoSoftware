package com.locatehub.demo.factory;

import java.math.BigDecimal;

import com.locatehub.demo.model.Ativo;

public interface AtivoFactory {
    Ativo criarAtivo(Long id, String titulo, BigDecimal diaria);
}