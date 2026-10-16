package br.com.fiap.bank.core.application.service;

import br.com.fiap.bank.core.application.dto.AtualizarClienteRequest;
import br.com.fiap.bank.core.application.dto.ClienteResponse;
import br.com.fiap.bank.core.application.dto.CriarClienteRequest;
import br.com.fiap.bank.core.domain.exception.ConflitoNegocioException;
import br.com.fiap.bank.core.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.bank.core.domain.model.Cliente;
import br.com.fiap.bank.core.domain.model.Cpf;
import br.com.fiap.bank.core.domain.model.Email;
import br.com.fiap.bank.core.domain.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<ClienteResponse> listarTodos() {
        return clienteRepository.listarTodos()
                .stream()
                .map(ClienteResponse::fromDomain)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(UUID id) {
        Cliente cliente = clienteRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com o ID: " + id));
        return ClienteResponse.fromDomain(cliente);
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorCpf(String cpf) {
        Cpf cpfValidado = new Cpf(cpf);
        Cliente cliente = clienteRepository.buscarPorCpf(cpfValidado.getValor())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado para o CPF: " + cpfValidado.getValor()));
        return ClienteResponse.fromDomain(cliente);
    }

    @Transactional
    public ClienteResponse criar(CriarClienteRequest request) {
        Cpf cpf = new Cpf(request.cpf());
        if (clienteRepository.existePorCpf(cpf.getValor())) {
            throw new ConflitoNegocioException("Já existe um cliente cadastrado com o CPF: " + cpf.getValor());
        }

        Cliente novoCliente = new Cliente(
                UUID.randomUUID(),
                request.nome(),
                new Email(request.email()),
                request.dataNascimento(),
                cpf,
                request.rendaMensal(),
                null
        );

        Cliente salvo = clienteRepository.salvar(novoCliente);
        return ClienteResponse.fromDomain(salvo);
    }

    @Transactional
    public ClienteResponse atualizar(UUID id, AtualizarClienteRequest request) {
        Cliente cliente = clienteRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com o ID: " + id));

        cliente.atualizarDados(
                request.nome(),
                new Email(request.email()),
                request.dataNascimento(),
                request.rendaMensal()
        );

        Cliente atualizado = clienteRepository.salvar(cliente);
        return ClienteResponse.fromDomain(atualizado);
    }

    @Transactional
    public void remover(UUID id) {
        if (clienteRepository.buscarPorId(id).isEmpty()) {
            throw new RecursoNaoEncontradoException("Cliente não encontrado com o ID: " + id);
        }
        clienteRepository.deletarPorId(id);
    }
}
