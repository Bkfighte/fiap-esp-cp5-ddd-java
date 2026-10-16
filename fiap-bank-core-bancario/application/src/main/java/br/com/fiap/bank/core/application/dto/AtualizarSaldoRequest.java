package br.com.fiap.bank.core.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record AtualizarSaldoRequest(
        @NotBlank(message = "Operação é obrigatória")
        @Pattern(regexp = "(?i)^(DEPOSITO|SAQUE)$", message = "Operação deve ser DEPOSITO ou SAQUE")
        String operacao,

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        BigDecimal valor
) {
}
