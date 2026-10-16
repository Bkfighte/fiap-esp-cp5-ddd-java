package br.com.fiap.bank.core.application.service;

import br.com.fiap.bank.core.application.dto.AtualizarSaldoRequest;
import br.com.fiap.bank.core.application.dto.ContaResponse;
import br.com.fiap.bank.core.application.dto.CriarContaRequest;
import br.com.fiap.bank.core.domain.exception.ConflitoNegocioException;
import br.com.fiap.bank.core.domain.exception.DominioException;
import br.com.fiap.bank.core.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.bank.core.domain.model.Cliente;
import br.com.fiap.bank.core.domain.model.ContaBancaria;
import br.com.fiap.bank.core.domain.model.Cpf;
import br.com.fiap.bank.core.domain.model.Saldo;
import br.com.fiap.bank.core.domain.model.StatusConta;
import br.com.fiap.bank.core.domain.repository.ClienteRepository;
import br.com.fiap.bank.core.domain.repository.ContaBancariaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContaBancariaService {

    private final ContaBancariaRepository contaRepository;
    private final ClienteRepository clienteRepository;
    private final Random random = new Random();

    @Transactional(readOnly = true)
    public List<ContaResponse> listarTodas() {
        return contaRepository.listarTodas()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ContaResponse buscarPorNumero(String numeroConta) {
        validarNumeroConta(numeroConta);
        ContaBancaria conta = contaRepository.buscarPorNumero(numeroConta.trim())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Conta não encontrada: " + numeroConta));
        return toResponse(conta);
    }

    @Transactional(readOnly = true)
    public ContaResponse buscarPorCpfCliente(String cpf) {
        Cpf cpfValidado = new Cpf(cpf);
        ContaBancaria conta = contaRepository.buscarPorCpfCliente(cpfValidado.getValor())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Conta não encontrada para o CPF: " + cpfValidado.getValor()));
        return toResponse(conta);
    }

    @Transactional
    public ContaResponse abrirConta(CriarContaRequest request) {
        return abrirContaComNumero(request, gerarNumeroConta());
    }

    @Transactional
    public ContaResponse abrirContaComNumero(CriarContaRequest request, String numeroConta) {
        Cpf cpf = new Cpf(request.cpfCliente());
        Cliente cliente = clienteRepository.buscarPorCpf(cpf.getValor())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado para o CPF: " + cpf.getValor()));

        if (contaRepository.existePorCpfCliente(cpf.getValor())) {
            throw new ConflitoNegocioException("Cliente já possui uma conta ativa cadastrada");
        }

        String agencia = (request.agencia() == null || request.agencia().isBlank()) ? "0001" : request.agencia().trim();

        ContaBancaria novaConta = new ContaBancaria(
                UUID.randomUUID(),
                numeroConta,
                agencia,
                new Saldo(request.saldoInicial()),
                LocalDate.now(),
                StatusConta.ATIVA,
                request.tipoConta(),
                cpf
        );

        ContaBancaria salva = contaRepository.salvar(novaConta);
        return ContaResponse.fromDomain(salva, cliente.getNome());
    }

    @Transactional
    public ContaResponse atualizarSaldo(String numeroConta, AtualizarSaldoRequest request) {
        validarNumeroConta(numeroConta);
        ContaBancaria conta = contaRepository.buscarPorNumero(numeroConta.trim())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Conta não encontrada: " + numeroConta));

        String operacao = request.operacao().trim().toUpperCase();
        if ("DEPOSITO".equals(operacao)) {
            conta.depositar(request.valor());
        } else if ("SAQUE".equals(operacao)) {
            conta.sacar(request.valor());
        } else {
            throw new DominioException("Operação inválida, use DEPOSITO ou SAQUE");
        }

        ContaBancaria atualizada = contaRepository.salvar(conta);
        return toResponse(atualizada);
    }

    @Transactional
    public void encerrarConta(String numeroConta) {
        validarNumeroConta(numeroConta);
        ContaBancaria conta = contaRepository.buscarPorNumero(numeroConta.trim())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Conta não encontrada: " + numeroConta));

        conta.encerrar();
        contaRepository.salvar(conta);
    }

    private ContaResponse toResponse(ContaBancaria conta) {
        String nomeCliente = clienteRepository.buscarPorCpf(conta.getCpfCliente().getValor())
                .map(Cliente::getNome)
                .orElse("");
        return ContaResponse.fromDomain(conta, nomeCliente);
    }

    private void validarNumeroConta(String numeroConta) {
        if (numeroConta == null || numeroConta.isBlank() || !numeroConta.trim().matches("^\\d{4,10}(-\\d)?$")) {
            throw new DominioException("Número da conta em formato inválido");
        }
    }

    private String gerarNumeroConta() {
        String numero;
        do {
            int base = 10000 + random.nextInt(90000);
            int digito = random.nextInt(10);
            numero = base + "-" + digito;
        } while (contaRepository.existePorNumero(numero));
        return numero;
    }
}
