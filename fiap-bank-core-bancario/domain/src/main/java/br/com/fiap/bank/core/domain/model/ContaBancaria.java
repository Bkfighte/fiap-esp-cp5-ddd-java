package br.com.fiap.bank.core.domain.model;

import br.com.fiap.bank.core.domain.exception.ConflitoNegocioException;
import br.com.fiap.bank.core.domain.exception.DominioException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class ContaBancaria {

    private final UUID id;
    private final String numero;
    private final String agencia;
    private Saldo saldo;
    private final LocalDate dataAbertura;
    private StatusConta status;
    private final TipoConta tipo;
    private final Cpf cpfCliente;

    public ContaBancaria(UUID id, String numero, String agencia, Saldo saldo, LocalDate dataAbertura,
                         StatusConta status, TipoConta tipo, Cpf cpfCliente) {
        if (numero == null || numero.isBlank()) {
            throw new DominioException("Número da conta é obrigatório");
        }
        // se nao vier agencia, usa 0001 como padrao
        String agenciaConta = (agencia == null || agencia.isBlank()) ? "0001" : agencia.trim();
        if (!agenciaConta.matches("\\d{4}")) {
            throw new DominioException("Agência inválida, informe 4 dígitos");
        }
        if (saldo == null) {
            throw new DominioException("Saldo inicial é obrigatório");
        }
        if (tipo == null) {
            throw new DominioException("Tipo da conta é obrigatório");
        }
        if (cpfCliente == null) {
            throw new DominioException("CPF do cliente é obrigatório");
        }

        this.id = id != null ? id : UUID.randomUUID();
        this.numero = numero.trim();
        this.agencia = agenciaConta;
        this.saldo = saldo;
        this.dataAbertura = dataAbertura != null ? dataAbertura : LocalDate.now();
        this.status = status != null ? status : StatusConta.ATIVA;
        this.tipo = tipo;
        this.cpfCliente = cpfCliente;
    }

    public Saldo getSaldo() {
        return saldo;
    }

    public void depositar(BigDecimal quantia) {
        validarContaAtiva();
        this.saldo = this.saldo.adicionar(quantia);
    }

    public void sacar(BigDecimal quantia) {
        validarContaAtiva();
        this.saldo = this.saldo.subtrair(quantia);
    }

    public void encerrar() {
        this.status = StatusConta.ENCERRADA;
    }

    public void bloquear() {
        if (this.status == StatusConta.ENCERRADA) {
            throw new ConflitoNegocioException("Conta já está encerrada");
        }
        this.status = StatusConta.BLOQUEADA;
    }

    private void validarContaAtiva() {
        if (this.status != StatusConta.ATIVA) {
            throw new ConflitoNegocioException("Conta não está ativa para movimentação (status: " + this.status + ")");
        }
    }
}
