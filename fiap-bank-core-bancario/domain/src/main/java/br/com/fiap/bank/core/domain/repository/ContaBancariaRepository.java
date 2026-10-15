package br.com.fiap.bank.core.domain.repository;

import br.com.fiap.bank.core.domain.model.ContaBancaria;

import java.util.List;
import java.util.Optional;

public interface ContaBancariaRepository {

    ContaBancaria salvar(ContaBancaria conta);

    Optional<ContaBancaria> buscarPorNumero(String numeroConta);

    Optional<ContaBancaria> buscarPorCpfCliente(String cpf);

    List<ContaBancaria> listarTodas();

    boolean existePorCpfCliente(String cpf);

    boolean existePorNumero(String numeroConta);
}
