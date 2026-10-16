package br.com.fiap.bank.core.presentation.controller;

import br.com.fiap.bank.core.application.dto.AtualizarSaldoRequest;
import br.com.fiap.bank.core.application.dto.ContaResponse;
import br.com.fiap.bank.core.application.dto.CriarContaRequest;
import br.com.fiap.bank.core.application.service.ContaBancariaService;
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

@RestController
@RequestMapping("/api/v1/contas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ContaBancariaController {

    private final ContaBancariaService contaBancariaService;

    @GetMapping
    public ResponseEntity<List<ContaResponse>> listarTodas() {
        return ResponseEntity.ok(contaBancariaService.listarTodas());
    }

    @GetMapping("/{numeroConta}")
    public ResponseEntity<ContaResponse> buscarPorNumero(@PathVariable String numeroConta) {
        return ResponseEntity.ok(contaBancariaService.buscarPorNumero(numeroConta));
    }

    @GetMapping("/cliente/{cpf}")
    public ResponseEntity<ContaResponse> buscarPorCpfCliente(@PathVariable String cpf) {
        return ResponseEntity.ok(contaBancariaService.buscarPorCpfCliente(cpf));
    }

    @PostMapping
    public ResponseEntity<ContaResponse> abrirConta(@Valid @RequestBody CriarContaRequest request) {
        ContaResponse criada = contaBancariaService.abrirConta(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @PutMapping("/{numeroConta}/saldo")
    public ResponseEntity<ContaResponse> atualizarSaldo(
            @PathVariable String numeroConta,
            @Valid @RequestBody AtualizarSaldoRequest request
    ) {
        return ResponseEntity.ok(contaBancariaService.atualizarSaldo(numeroConta, request));
    }

    @DeleteMapping("/{numeroConta}")
    public ResponseEntity<Void> encerrarConta(@PathVariable String numeroConta) {
        contaBancariaService.encerrarConta(numeroConta);
        return ResponseEntity.noContent().build();
    }
}
