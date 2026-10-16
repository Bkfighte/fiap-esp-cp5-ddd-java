package br.com.fiap.bank.plataforma.application.dto;

import br.com.fiap.bank.plataforma.domain.model.SolicitacaoCredito;
import br.com.fiap.bank.plataforma.domain.model.StatusSolicitacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SolicitacaoCreditoResponseDTO(
        String id,
        String cpfCliente,
        String nomeCliente,
        String tipoCredito,
        BigDecimal valorSolicitado,
        Integer quantidadeParcelas,
        BigDecimal rendaMensalDeclarada,
        StatusSolicitacao status,
        Integer scoreBureau,
        Boolean scrRestricoes,
        String parecerAnalise,
        LocalDateTime dataSolicitacao,
        LocalDateTime dataFinalizacao
) {
    public static SolicitacaoCreditoResponseDTO fromEntity(SolicitacaoCredito entity) {
        return new SolicitacaoCreditoResponseDTO(
                entity.getId(),
                entity.getCpfCliente(),
                entity.getNomeCliente(),
                entity.getTipoCredito(),
                entity.getValorSolicitado(),
                entity.getQuantidadeParcelas(),
                entity.getRendaMensalDeclarada(),
                entity.getStatus(),
                entity.getScoreBureau(),
                entity.getScrRestricoes(),
                entity.getParecerAnalise(),
                entity.getDataSolicitacao(),
                entity.getDataFinalizacao()
        );
    }
}
