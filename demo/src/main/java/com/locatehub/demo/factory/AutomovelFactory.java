package com.locatehub.demo.factory;

import java.math.BigDecimal;
import com.locatehub.demo.model.Ativo;
import com.locatehub.demo.model.AtivoAutomovel;

public class AutomovelFactory implements AtivoFactory {
    @Override
    public Ativo criarAtivo(Long id, String titulo, BigDecimal diaria) {
        System.out.println("[AutomovelFactory] Montando veículo com seguro diário padrão...");
        BigDecimal seguroPadrao = new BigDecimal("50.00");
        return new AtivoAutomovel(id, titulo, diaria, seguroPadrao);
    }
}
