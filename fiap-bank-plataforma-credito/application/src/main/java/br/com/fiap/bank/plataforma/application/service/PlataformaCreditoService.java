package br.com.fiap.bank.plataforma.application.service;

import br.com.fiap.bank.plataforma.application.dto.SolicitacaoCreditoResponseDTO;
import br.com.fiap.bank.plataforma.application.dto.SolicitarCreditoRequest;
import br.com.fiap.bank.plataforma.domain.exception.DominioException;
import br.com.fiap.bank.plataforma.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.bank.plataforma.domain.gateway.BureauCreditoGateway;
import br.com.fiap.bank.plataforma.domain.gateway.CoreBancarioGateway;
import br.com.fiap.bank.plataforma.domain.gateway.NotificacaoGateway;
import br.com.fiap.bank.plataforma.domain.gateway.ScrGateway;
import br.com.fiap.bank.plataforma.domain.model.SolicitacaoCredito;
import br.com.fiap.bank.plataforma.domain.model.StatusSolicitacao;
import br.com.fiap.bank.plataforma.domain.repository.SolicitacaoCreditoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlataformaCreditoService {

    private final SolicitacaoCreditoRepository repository;
    private final BureauCreditoGateway bureauCreditoGateway;
    private final ScrGateway scrGateway;
    private final NotificacaoGateway notificacaoGateway;
    private final CoreBancarioGateway coreBancarioGateway;

    @Transactional
    public SolicitacaoCreditoResponseDTO processarNovaSolicitacao(SolicitarCreditoRequest request) {
        SolicitacaoCredito solicitacao = new SolicitacaoCredito(
                request.cpfCliente(),
                request.nomeCliente(),
                request.tipoCredito(),
                request.valorSolicitado(),
                request.quantidadeParcelas(),
                request.rendaMensalDeclarada()
        );

        Map<String, Object> dadosConta = coreBancarioGateway.consultarContaCliente(solicitacao.getCpfCliente());
        String statusConta = dadosConta != null && dadosConta.get("statusConta") != null
                ? dadosConta.get("statusConta").toString()
                : "ATIVA";

        int score = bureauCreditoGateway.consultarScore(solicitacao.getCpfCliente());
        boolean possuiRestricoesScr = scrGateway.possuiRestricoesBacen(solicitacao.getCpfCliente());

        BigDecimal valorParcelaEstimada = solicitacao.getValorSolicitado()
                .divide(BigDecimal.valueOf(solicitacao.getQuantidadeParcelas()), 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("1.02"));

        BigDecimal limiteComprometimentoRenda = solicitacao.getRendaMensalDeclarada().multiply(new BigDecimal("0.35"));

        if (!"ATIVA".equalsIgnoreCase(statusConta)) {
            solicitacao.registrarAnaliseAutomatica(
                    StatusSolicitacao.REPROVADO_POLITICA,
                    score,
                    possuiRestricoesScr,
                    "Reprovado automaticamente: Conta bancária no Core Bancário não está ATIVA."
            );
        } else if (possuiRestricoesScr || score < 400) {
            solicitacao.registrarAnaliseAutomatica(
                    StatusSolicitacao.REPROVADO_POLITICA,
                    score,
                    possuiRestricoesScr,
                    String.format("Reprovado pela política automática: Score (%d) abaixo do mínimo ou apontamento restritivo no SCR/BACEN.", score)
            );
        } else if (valorParcelaEstimada.compareTo(limiteComprometimentoRenda) > 0) {
            solicitacao.registrarAnaliseAutomatica(
                    StatusSolicitacao.EM_ANALISE,
                    score,
                    possuiRestricoesScr,
                    "Encaminhado para Mesa de Operações: Parcela estimada excede 35% da renda declarada."
            );
        } else if (score >= 700 && solicitacao.getValorSolicitado().compareTo(new BigDecimal("50000")) <= 0) {
            solicitacao.registrarAnaliseAutomatica(
                    StatusSolicitacao.PRE_APROVADO,
                    score,
                    possuiRestricoesScr,
                    String.format("Pré-aprovado pelo motor de regras (Score %d). Aguardando validação final da Mesa de Operações.", score)
            );
        } else {
            solicitacao.registrarAnaliseAutomatica(
                    StatusSolicitacao.EM_ANALISE,
                    score,
                    possuiRestricoesScr,
                    "Em análise de crédito pela Mesa de Operações."
            );
        }

        SolicitacaoCredito salva = repository.salvar(solicitacao);
        notificacaoGateway.notificarCliente(salva.getCpfCliente(), salva.getStatus().name(), salva.getParecerAnalise());

        return SolicitacaoCreditoResponseDTO.fromEntity(salva);
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoCreditoResponseDTO> consultarPorCpf(String cpfCliente) {
        if (cpfCliente == null || !cpfCliente.replaceAll("\\D", "").matches("^\\d{11}$")) {
            throw new DominioException("CPF inválido. Informe 11 dígitos numéricos.");
        }
        String cpfLimpo = cpfCliente.replaceAll("\\D", "");
        return repository.buscarPorCpfCliente(cpfLimpo)
                .stream()
                .map(SolicitacaoCreditoResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoCreditoResponseDTO> listarTodasSolicitacoes() {
        return repository.listarTodasOrdenadasPorData()
                .stream()
                .map(SolicitacaoCreditoResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public SolicitacaoCreditoResponseDTO buscarPorId(String solicitacaoId) {
        if (solicitacaoId == null || solicitacaoId.isBlank()) {
            throw new DominioException("O identificador da solicitação é obrigatório.");
        }
        SolicitacaoCredito solicitacao = repository.buscarPorId(solicitacaoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação de crédito não encontrada para o ID: " + solicitacaoId));
        return SolicitacaoCreditoResponseDTO.fromEntity(solicitacao);
    }

    @Transactional
    public SolicitacaoCreditoResponseDTO aprovarSolicitacao(String solicitacaoId, String parecerOperador) {
        if (solicitacaoId == null || solicitacaoId.isBlank()) {
            throw new DominioException("O identificador da solicitação é obrigatório.");
        }
        SolicitacaoCredito solicitacao = repository.buscarPorId(solicitacaoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação de crédito não encontrada para o ID: " + solicitacaoId));

        solicitacao.aprovarPelaMesa(parecerOperador);

        SolicitacaoCredito salva = repository.salvar(solicitacao);
        notificacaoGateway.notificarCliente(salva.getCpfCliente(), salva.getStatus().name(), salva.getParecerAnalise());

        return SolicitacaoCreditoResponseDTO.fromEntity(salva);
    }

    @Transactional
    public SolicitacaoCreditoResponseDTO reprovarSolicitacao(String solicitacaoId, String motivoReprovacao) {
        if (solicitacaoId == null || solicitacaoId.isBlank()) {
            throw new DominioException("O identificador da solicitação é obrigatório.");
        }
        SolicitacaoCredito solicitacao = repository.buscarPorId(solicitacaoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação de crédito não encontrada para o ID: " + solicitacaoId));

        solicitacao.reprovarPelaMesa(motivoReprovacao);

        SolicitacaoCredito salva = repository.salvar(solicitacao);
        notificacaoGateway.notificarCliente(salva.getCpfCliente(), salva.getStatus().name(), salva.getParecerAnalise());

        return SolicitacaoCreditoResponseDTO.fromEntity(salva);
    }

    @Transactional
    public void removerSolicitacao(String solicitacaoId) {
        if (solicitacaoId == null || solicitacaoId.isBlank()) {
            throw new DominioException("O identificador da solicitação é obrigatório.");
        }
        if (repository.buscarPorId(solicitacaoId).isEmpty()) {
            throw new RecursoNaoEncontradoException("Solicitação de crédito não encontrada para o ID: " + solicitacaoId);
        }
        repository.deletarPorId(solicitacaoId);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> consultarDadosContaCore(String cpfCliente) {
        if (cpfCliente == null || !cpfCliente.replaceAll("\\D", "").matches("^\\d{11}$")) {
            throw new DominioException("CPF inválido. Informe 11 dígitos numéricos.");
        }
        return coreBancarioGateway.consultarContaCliente(cpfCliente.replaceAll("\\D", ""));
    }

    @Transactional(readOnly = true)
    public boolean possuiSolicitacoesCadastradas() {
        return repository.contar() > 0;
    }
}
