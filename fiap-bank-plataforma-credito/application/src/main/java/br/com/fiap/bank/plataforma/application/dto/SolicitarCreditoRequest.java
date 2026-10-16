package br.com.fiap.bank.plataforma.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record SolicitarCreditoRequest(
        @NotBlank(message = "O CPF do cliente é obrigatório")
        @Pattern(regexp = "^\\d{11}$", message = "O CPF deve conter exatamente 11 dígitos numéricos")
        String cpfCliente,

        @NotBlank(message = "O nome do cliente é obrigatório")
        String nomeCliente,

        @NotBlank(message = "A modalidade de crédito é obrigatória")
        String tipoCredito,

        @NotNull(message = "O valor solicitado é obrigatório")
        @DecimalMin(value = "0.01", message = "O valor solicitado deve ser maior que zero")
        BigDecimal valorSolicitado,

        @NotNull(message = "A quantidade de parcelas é obrigatória")
        @Min(value = 1, message = "A quantidade mínima de parcelas é 1")
        @Max(value = 360, message = "A quantidade máxima de parcelas é 360")
        Integer quantidadeParcelas,

        @NotNull(message = "A renda mensal declarada é obrigatória")
        @DecimalMin(value = "0.01", message = "A renda mensal declarada deve ser maior que zero")
        BigDecimal rendaMensalDeclarada
) {
}
