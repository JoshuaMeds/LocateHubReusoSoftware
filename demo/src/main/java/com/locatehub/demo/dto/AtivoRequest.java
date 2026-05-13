package com.locatehub.demo.dto;

import java.math.BigDecimal;

public record AtivoRequest(
        String type,
        String titulo,
        String descricao,
        BigDecimal valorDiaria,
        BigDecimal seguroDiario,
        BigDecimal taxaLimpeza,
        boolean caucao,
        BigDecimal valorCaucao
) {}