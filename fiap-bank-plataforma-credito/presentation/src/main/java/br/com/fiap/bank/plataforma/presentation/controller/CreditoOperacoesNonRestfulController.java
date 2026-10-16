package br.com.fiap.bank.plataforma.presentation.controller;

import br.com.fiap.bank.plataforma.application.dto.AprovarSolicitacaoRequest;
import br.com.fiap.bank.plataforma.application.dto.BuscarSolicitacaoRequest;
import br.com.fiap.bank.plataforma.application.dto.ConsultarStatusRequest;
import br.com.fiap.bank.plataforma.application.dto.ReprovarSolicitacaoRequest;
import br.com.fiap.bank.plataforma.application.dto.SolicitacaoCreditoResponseDTO;
import br.com.fiap.bank.plataforma.application.dto.SolicitarCreditoRequest;
import br.com.fiap.bank.plataforma.application.service.PlataformaCreditoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

// Mantido como adapter para nao quebrar o front da Mesa de Operacoes (8082) e o app Angular (4200)
@RestController
@RequestMapping("/api/credito")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CreditoOperacoesNonRestfulController {

    private final PlataformaCreditoService service;

    @PostMapping("/solicitar")
    public ResponseEntity<SolicitacaoCreditoResponseDTO> solicitarCredito(@Valid @RequestBody SolicitarCreditoRequest request) {
        SolicitacaoCreditoResponseDTO response = service.processarNovaSolicitacao(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/consultar-status")
    public ResponseEntity<List<SolicitacaoCreditoResponseDTO>> consultarStatusPorCpf(@Valid @RequestBody ConsultarStatusRequest request) {
        List<SolicitacaoCreditoResponseDTO> lista = service.consultarPorCpf(request.cpfCliente());
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/listar-todas")
    public ResponseEntity<List<SolicitacaoCreditoResponseDTO>> listarTodasSolicitacoes() {
        List<SolicitacaoCreditoResponseDTO> lista = service.listarTodasSolicitacoes();
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/buscar-detalhe")
    public ResponseEntity<SolicitacaoCreditoResponseDTO> buscarDetalhe(@Valid @RequestBody BuscarSolicitacaoRequest request) {
        SolicitacaoCreditoResponseDTO response = service.buscarPorId(request.solicitacaoId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/aprovar-operacao")
    public ResponseEntity<SolicitacaoCreditoResponseDTO> aprovarOperacao(@RequestBody AprovarSolicitacaoRequest request) {
        SolicitacaoCreditoResponseDTO response = service.aprovarSolicitacao(request.solicitacaoId(), request.parecerOperador());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reprovar-operacao")
    public ResponseEntity<SolicitacaoCreditoResponseDTO> reprovarOperacao(@Valid @RequestBody ReprovarSolicitacaoRequest request) {
        SolicitacaoCreditoResponseDTO response = service.reprovarSolicitacao(request.solicitacaoId(), request.motivoReprovacao());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/consultar-conta-core")
    public ResponseEntity<Map<String, Object>> consultarContaCore(@Valid @RequestBody ConsultarStatusRequest request) {
        Map<String, Object> dadosCore = service.consultarDadosContaCore(request.cpfCliente());
        return ResponseEntity.ok(dadosCore);
    }
}
