package br.com.fiap.bank.core.domain.model;

import br.com.fiap.bank.core.domain.exception.ConflitoNegocioException;
import br.com.fiap.bank.core.domain.exception.DominioException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@EqualsAndHashCode
public class Saldo {

    private final BigDecimal valor;

    public Saldo(BigDecimal valor) {
        if (valor == null) {
            throw new DominioException("Saldo não pode ser nulo");
        }
        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new DominioException("Saldo não pode ser negativo");
        }
        this.valor = valor.setScale(2, RoundingMode.HALF_UP);
    }

    public Saldo adicionar(BigDecimal quantia) {
        if (quantia == null || quantia.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DominioException("Valor de depósito deve ser maior que zero");
        }
        return new Saldo(this.valor.add(quantia));
    }

    public Saldo subtrair(BigDecimal quantia) {
        if (quantia == null || quantia.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DominioException("Valor de saque deve ser maior que zero");
        }
        if (this.valor.compareTo(quantia) < 0) {
            throw new ConflitoNegocioException("Saldo insuficiente para realizar o saque");
        }
        return new Saldo(this.valor.subtract(quantia));
    }

    @Override
    public String toString() {
        return valor.toPlainString();
    }
}
