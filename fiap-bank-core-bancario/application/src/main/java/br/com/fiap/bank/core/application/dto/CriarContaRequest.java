package br.com.fiap.bank.core.application.dto;

import br.com.fiap.bank.core.domain.model.TipoConta;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record CriarContaRequest(
        @NotBlank(message = "CPF do cliente é obrigatório")
        @Pattern(regexp = "^\\d{11}$", message = "CPF deve ter 11 dígitos numéricos")
        String cpfCliente,

        @Pattern(regexp = "^$|^\\d{4}$", message = "Agência deve ter 4 dígitos numéricos")
        String agencia,

        @NotNull(message = "Tipo da conta é obrigatório")
        TipoConta tipoConta,

        @NotNull(message = "Saldo inicial é obrigatório")
        @DecimalMin(value = "0.00", message = "Saldo inicial não pode ser negativo")
        BigDecimal saldoInicial
) {
}
