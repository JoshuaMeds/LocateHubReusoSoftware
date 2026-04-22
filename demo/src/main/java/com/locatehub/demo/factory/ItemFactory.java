package com.locatehub.demo.factory;

import java.math.BigDecimal;
import com.locatehub.demo.model.Ativo;
import com.locatehub.demo.model.AtivoItem;

public class ItemFactory implements AtivoFactory {
    @Override
    public Ativo criarAtivo(Long id, String titulo, BigDecimal diaria) {
        System.out.println("[ItemFactory] Montando item com caução exigida...");
        BigDecimal caucaoPadrao = new BigDecimal("100.00");
        return new AtivoItem(id, titulo, diaria, true, caucaoPadrao);
    }
}
