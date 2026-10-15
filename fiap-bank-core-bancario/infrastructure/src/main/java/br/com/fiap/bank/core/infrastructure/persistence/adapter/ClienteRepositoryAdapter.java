package br.com.fiap.bank.core.infrastructure.persistence.adapter;

import br.com.fiap.bank.core.domain.model.Cliente;
import br.com.fiap.bank.core.domain.model.ContaBancaria;
import br.com.fiap.bank.core.domain.model.Cpf;
import br.com.fiap.bank.core.domain.model.Email;
import br.com.fiap.bank.core.domain.repository.ClienteRepository;
import br.com.fiap.bank.core.infrastructure.persistence.entity.ClienteJpaEntity;
import br.com.fiap.bank.core.infrastructure.persistence.repository.SpringDataClienteRepository;
import br.com.fiap.bank.core.infrastructure.persistence.repository.SpringDataContaBancariaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ClienteRepositoryAdapter implements ClienteRepository {

    private final SpringDataClienteRepository springDataClienteRepository;
    private final SpringDataContaBancariaRepository springDataContaRepository;
    private final ContaBancariaRepositoryAdapter contaAdapter;

    @Override
    public Cliente salvar(Cliente cliente) {
        ClienteJpaEntity entity = toEntity(cliente);
        ClienteJpaEntity saved = springDataClienteRepository.save(entity);
        if (cliente.getConta() != null) {
            springDataContaRepository.save(contaAdapter.toEntity(cliente.getConta()));
        }
        return toDomain(saved);
    }

    @Override
    public Optional<Cliente> buscarPorId(UUID id) {
        return springDataClienteRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Cliente> buscarPorCpf(String cpf) {
        return springDataClienteRepository.findByCpf(cpf).map(this::toDomain);
    }

    @Override
    public List<Cliente> listarTodos() {
        return springDataClienteRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existePorCpf(String cpf) {
        return springDataClienteRepository.existsByCpf(cpf);
    }

    @Override
    public void deletarPorId(UUID id) {
        springDataClienteRepository.findById(id).ifPresent(entity -> {
            springDataContaRepository.findByCpfCliente(entity.getCpf())
                    .ifPresent(springDataContaRepository::delete);
            springDataClienteRepository.deleteById(id);
        });
    }

    private ClienteJpaEntity toEntity(Cliente domain) {
        return new ClienteJpaEntity(
                domain.getId(),
                domain.getCpf().getValor(),
                domain.getNome(),
                domain.getEmail().getValor(),
                domain.getDataNascimento(),
                domain.getRendaMensal()
        );
    }

    private Cliente toDomain(ClienteJpaEntity entity) {
        ContaBancaria conta = springDataContaRepository.findByCpfCliente(entity.getCpf())
                .map(contaAdapter::toDomain)
                .orElse(null);

        return new Cliente(
                entity.getId(),
                entity.getNome(),
                new Email(entity.getEmail()),
                entity.getDataNascimento(),
                new Cpf(entity.getCpf()),
                entity.getRendaMensal(),
                conta
        );
    }
}
