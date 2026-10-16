package br.com.fiap.bank.plataforma.application.dto;

public record AprovarSolicitacaoRequest(
        String solicitacaoId,
        String parecerOperador
) {
}
