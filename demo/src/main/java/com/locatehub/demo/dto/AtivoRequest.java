package com.locatehub.demo.dto;

import java.math.BigDecimal;

public record AtivoRequest(String type, String titulo, BigDecimal valorDiaria, Long donoId, String descricao) {
}
