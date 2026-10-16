package br.com.fiap.bank.plataforma.application.dto;

import jakarta.validation.constraints.NotBlank;

public record BuscarSolicitacaoRequest(
        @NotBlank(message = "O ID da solicitação é obrigatório")
        String solicitacaoId
) {
}
