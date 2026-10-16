package br.com.fiap.bank.plataforma.domain.model;

import br.com.fiap.bank.plataforma.domain.exception.ConflitoNegocioException;
import br.com.fiap.bank.plataforma.domain.exception.DominioException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class SolicitacaoCredito {

    private String id;
    private String cpfCliente;
    private String nomeCliente;
    private String tipoCredito;
    private BigDecimal valorSolicitado;
    private Integer quantidadeParcelas;
    private BigDecimal rendaMensalDeclarada;
    private StatusSolicitacao status;
    private Integer scoreBureau;
    private Boolean scrRestricoes;
    private String parecerAnalise;
    private LocalDateTime dataSolicitacao;
    private LocalDateTime dataFinalizacao;

    public SolicitacaoCredito(String cpfCliente, String nomeCliente, String tipoCredito,
                              BigDecimal valorSolicitado, Integer quantidadeParcelas,
                              BigDecimal rendaMensalDeclarada) {
        if (cpfCliente == null || !cpfCliente.replaceAll("\\D", "").matches("^\\d{11}$")) {
            throw new DominioException("O CPF do cliente deve conter 11 dígitos numéricos.");
        }
        if (nomeCliente == null || nomeCliente.isBlank()) {
            throw new DominioException("O nome do cliente é obrigatório.");
        }
        if (tipoCredito == null || tipoCredito.isBlank()) {
            throw new DominioException("A modalidade de crédito é obrigatória.");
        }
        if (valorSolicitado == null || valorSolicitado.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DominioException("O valor solicitado deve ser maior que zero.");
        }
        if (quantidadeParcelas == null || quantidadeParcelas <= 0) {
            throw new DominioException("A quantidade de parcelas deve ser maior que zero.");
        }
        if (rendaMensalDeclarada == null || rendaMensalDeclarada.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DominioException("A renda mensal declarada deve ser maior que zero.");
        }

        this.id = UUID.randomUUID().toString();
        this.cpfCliente = cpfCliente.replaceAll("\\D", "");
        this.nomeCliente = nomeCliente.trim();
        this.tipoCredito = tipoCredito.trim();
        this.valorSolicitado = valorSolicitado;
        this.quantidadeParcelas = quantidadeParcelas;
        this.rendaMensalDeclarada = rendaMensalDeclarada;
        this.status = StatusSolicitacao.EM_ANALISE;
        this.dataSolicitacao = LocalDateTime.now();
    }

    public void registrarAnaliseAutomatica(StatusSolicitacao statusCalculado, Integer scoreBureau,
                                           Boolean scrRestricoes, String parecerAnalise) {
        this.status = statusCalculado;
        this.scoreBureau = scoreBureau;
        this.scrRestricoes = scrRestricoes;
        this.parecerAnalise = parecerAnalise;
        if (statusCalculado == StatusSolicitacao.REPROVADO_POLITICA) {
            this.dataFinalizacao = LocalDateTime.now();
        }
    }

    public void aprovarPelaMesa(String parecerOperador) {
        if (this.status == StatusSolicitacao.APROVADO || this.status == StatusSolicitacao.REPROVADO_MESA) {
            throw new ConflitoNegocioException("Esta solicitação já foi finalizada pela Mesa de Operações com status " + this.status + ".");
        }
        this.status = StatusSolicitacao.APROVADO;
        this.dataFinalizacao = LocalDateTime.now();
        String observacao = (parecerOperador != null && !parecerOperador.isBlank())
                ? parecerOperador.trim()
                : "Sem observações adicionais.";
        this.parecerAnalise = "Aprovado pela Mesa de Operações: " + observacao;
    }

    public void reprovarPelaMesa(String motivoReprovacao) {
        if (this.status == StatusSolicitacao.APROVADO || this.status == StatusSolicitacao.REPROVADO_MESA) {
            throw new ConflitoNegocioException("Esta solicitação já foi finalizada pela Mesa de Operações com status " + this.status + ".");
        }
        if (motivoReprovacao == null || motivoReprovacao.isBlank()) {
            throw new DominioException("O motivo de reprovação pela Mesa de Operações é obrigatório.");
        }
        this.status = StatusSolicitacao.REPROVADO_MESA;
        this.dataFinalizacao = LocalDateTime.now();
        this.parecerAnalise = "Reprovado pela Mesa de Operações: " + motivoReprovacao.trim();
    }
}
