package br.com.fiap.bank.plataforma.presentation.controller;

import br.com.fiap.bank.plataforma.application.dto.AprovarSolicitacaoRequest;
import br.com.fiap.bank.plataforma.application.dto.ReprovarSolicitacaoRequest;
import br.com.fiap.bank.plataforma.application.dto.SolicitacaoCreditoResponseDTO;
import br.com.fiap.bank.plataforma.application.dto.SolicitarCreditoRequest;
import br.com.fiap.bank.plataforma.application.service.PlataformaCreditoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/solicitacoes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SolicitacaoCreditoRestController {

    private final PlataformaCreditoService service;

    @GetMapping
    public ResponseEntity<List<SolicitacaoCreditoResponseDTO>> listarTodas() {
        return ResponseEntity.ok(service.listarTodasSolicitacoes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoCreditoResponseDTO> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<List<SolicitacaoCreditoResponseDTO>> consultarPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(service.consultarPorCpf(cpf));
    }

    @GetMapping("/conta-core/{cpf}")
    public ResponseEntity<Map<String, Object>> consultarContaCore(@PathVariable String cpf) {
        return ResponseEntity.ok(service.consultarDadosContaCore(cpf));
    }

    @PostMapping
    public ResponseEntity<SolicitacaoCreditoResponseDTO> criar(@Valid @RequestBody SolicitarCreditoRequest request) {
        SolicitacaoCreditoResponseDTO criada = service.processarNovaSolicitacao(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @PutMapping("/{id}/aprovacao")
    public ResponseEntity<SolicitacaoCreditoResponseDTO> aprovar(
            @PathVariable String id,
            @RequestBody(required = false) AprovarSolicitacaoRequest request
    ) {
        String parecer = request != null ? request.parecerOperador() : null;
        return ResponseEntity.ok(service.aprovarSolicitacao(id, parecer));
    }

    @PutMapping("/{id}/reprovacao")
    public ResponseEntity<SolicitacaoCreditoResponseDTO> reprovar(
            @PathVariable String id,
            @Valid @RequestBody ReprovarSolicitacaoRequest request
    ) {
        return ResponseEntity.ok(service.reprovarSolicitacao(id, request.motivoReprovacao()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable String id) {
        service.removerSolicitacao(id);
        return ResponseEntity.noContent().build();
    }
}
