package br.com.fiap.bank.core.application.dto;

import br.com.fiap.bank.core.domain.model.ContaBancaria;
import br.com.fiap.bank.core.domain.model.StatusConta;
import br.com.fiap.bank.core.domain.model.TipoConta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ContaResponse(
        UUID id,
        String numeroConta,
        String agencia,
        String cpfCliente,
        String nomeCliente,
        TipoConta tipoConta,
        StatusConta statusConta,
        BigDecimal saldo,
        LocalDate dataAbertura
) {
    public static ContaResponse fromDomain(ContaBancaria conta, String nomeCliente) {
        if (conta == null) {
            return null;
        }
        return new ContaResponse(
                conta.getId(),
                conta.getNumero(),
                conta.getAgencia(),
                conta.getCpfCliente().getValor(),
                nomeCliente,
                conta.getTipo(),
                conta.getStatus(),
                conta.getSaldo().getValor(),
                conta.getDataAbertura()
        );
    }
}
