package br.com.fiap.bank.core.infrastructure.persistence.adapter;

import br.com.fiap.bank.core.domain.model.ContaBancaria;
import br.com.fiap.bank.core.domain.model.Cpf;
import br.com.fiap.bank.core.domain.model.Saldo;
import br.com.fiap.bank.core.domain.repository.ContaBancariaRepository;
import br.com.fiap.bank.core.infrastructure.persistence.entity.ContaBancariaJpaEntity;
import br.com.fiap.bank.core.infrastructure.persistence.repository.SpringDataContaBancariaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ContaBancariaRepositoryAdapter implements ContaBancariaRepository {

    private final SpringDataContaBancariaRepository springDataRepository;

    @Override
    public ContaBancaria salvar(ContaBancaria conta) {
        ContaBancariaJpaEntity entity = toEntity(conta);
        ContaBancariaJpaEntity saved = springDataRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<ContaBancaria> buscarPorNumero(String numeroConta) {
        return springDataRepository.findByNumero(numeroConta).map(this::toDomain);
    }

    @Override
    public Optional<ContaBancaria> buscarPorCpfCliente(String cpf) {
        return springDataRepository.findByCpfCliente(cpf).map(this::toDomain);
    }

    @Override
    public List<ContaBancaria> listarTodas() {
        return springDataRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existePorCpfCliente(String cpf) {
        return springDataRepository.existsByCpfCliente(cpf);
    }

    @Override
    public boolean existePorNumero(String numeroConta) {
        return springDataRepository.existsByNumero(numeroConta);
    }

    public ContaBancariaJpaEntity toEntity(ContaBancaria domain) {
        return new ContaBancariaJpaEntity(
                domain.getId(),
                domain.getNumero(),
                domain.getAgencia(),
                domain.getSaldo().getValor(),
                domain.getDataAbertura(),
                domain.getStatus(),
                domain.getTipo(),
                domain.getCpfCliente().getValor()
        );
    }

    public ContaBancaria toDomain(ContaBancariaJpaEntity entity) {
        return new ContaBancaria(
                entity.getId(),
                entity.getNumero(),
                entity.getAgencia(),
                new Saldo(entity.getSaldo()),
                entity.getDataAbertura(),
                entity.getStatus(),
                entity.getTipo(),
                new Cpf(entity.getCpfCliente())
        );
    }
}
