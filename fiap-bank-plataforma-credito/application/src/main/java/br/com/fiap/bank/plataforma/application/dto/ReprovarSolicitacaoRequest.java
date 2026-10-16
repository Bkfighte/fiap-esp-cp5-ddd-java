package br.com.fiap.bank.plataforma.application.dto;

import jakarta.validation.constraints.NotBlank;

public record ReprovarSolicitacaoRequest(
        String solicitacaoId,
        @NotBlank(message = "O motivo de reprovação é obrigatório")
        String motivoReprovacao
) {
}
