package com.locatehub.demo.factory;

import java.math.BigDecimal;
import com.locatehub.demo.model.Ativo;
import com.locatehub.demo.model.AtivoImovel;

public class ImovelFactory implements AtivoFactory {
    @Override
    public Ativo criarAtivo(Long id, String titulo, BigDecimal diaria) {
        System.out.println("[ImovelFactory] Montando imóvel com taxa de limpeza padrão...");
        BigDecimal taxaLimpezaPadrao = new BigDecimal("150.00");
        return new AtivoImovel(id, titulo, diaria, taxaLimpezaPadrao);
    }
}
