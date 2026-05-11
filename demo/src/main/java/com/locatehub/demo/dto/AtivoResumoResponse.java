package com.locatehub.demo.dto;

import com.locatehub.demo.model.Ativo;
import com.locatehub.demo.model.AtivoAutomovel;
import com.locatehub.demo.model.AtivoImovel;
import com.locatehub.demo.model.AtivoItem;

import java.math.BigDecimal;

public record AtivoResumoResponse(
        Long id,
        String titulo,
        String descricao,
        BigDecimal valorDiaria,
        boolean disponivel,
        String tipo,
        String tipoNome,
        boolean naListaDesejos,
        boolean podeUsarListaDesejos
) {
    public static AtivoResumoResponse from(Ativo ativo, boolean naListaDesejos) {
        return from(ativo, naListaDesejos, true);
    }

    public static AtivoResumoResponse from(Ativo ativo, boolean naListaDesejos, boolean podeUsarListaDesejos) {
        return new AtivoResumoResponse(
                ativo.getId(),
                ativo.getTitulo(),
                ativo.getDescricao(),
                ativo.getValorDiaria(),
                ativo.isDisponivel(),
                tipoCodigo(ativo),
                ativo.getTipo(),
                naListaDesejos,
                podeUsarListaDesejos
        );
    }

    public static String tipoCodigo(Ativo ativo) {
        if (ativo instanceof AtivoImovel) return "IMOVEL";
        if (ativo instanceof AtivoAutomovel) return "AUTOMOVEL";
        if (ativo instanceof AtivoItem) return "ITEM";
        return "ATIVO";
    }
}
