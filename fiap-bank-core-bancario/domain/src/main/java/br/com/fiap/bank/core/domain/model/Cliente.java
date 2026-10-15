package br.com.fiap.bank.core.domain.model;

import br.com.fiap.bank.core.domain.exception.DominioException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class Cliente {

    private final UUID id;
    private String nome;
    private Email email;
    private LocalDate dataNascimento;
    private final Cpf cpf;
    private BigDecimal rendaMensal;
    private ContaBancaria conta;

    public Cliente(UUID id, String nome, Email email, LocalDate dataNascimento, Cpf cpf,
                   BigDecimal rendaMensal, ContaBancaria conta) {
        validarDados(nome, email, dataNascimento, cpf, rendaMensal);
        this.id = id != null ? id : UUID.randomUUID();
        this.nome = nome.trim();
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.rendaMensal = rendaMensal.setScale(2, RoundingMode.HALF_UP);
        this.conta = conta;
    }

    public void atualizarDados(String nome, Email email, LocalDate dataNascimento, BigDecimal rendaMensal) {
        validarDados(nome, email, dataNascimento, this.cpf, rendaMensal);
        this.nome = nome.trim();
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.rendaMensal = rendaMensal.setScale(2, RoundingMode.HALF_UP);
    }

    public void vincularConta(ContaBancaria conta) {
        this.conta = conta;
    }

    private void validarDados(String nome, Email email, LocalDate dataNascimento, Cpf cpf, BigDecimal rendaMensal) {
        if (nome == null || nome.trim().length() < 3) {
            throw new DominioException("Nome do cliente deve ter pelo menos 3 caracteres");
        }
        if (email == null) {
            throw new DominioException("Email é obrigatório");
        }
        if (dataNascimento == null || !dataNascimento.isBefore(LocalDate.now())) {
            throw new DominioException("Data de nascimento deve estar no passado");
        }
        if (cpf == null) {
            throw new DominioException("CPF é obrigatório");
        }
        if (rendaMensal == null || rendaMensal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DominioException("Renda mensal deve ser maior que zero");
        }
    }
}
