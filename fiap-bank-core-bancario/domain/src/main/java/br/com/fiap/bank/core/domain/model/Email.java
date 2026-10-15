package br.com.fiap.bank.core.domain.model;

import br.com.fiap.bank.core.domain.exception.DominioException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Email {

    private final String valor;

    public Email(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new DominioException("Email é obrigatório");
        }
        String emailFormatado = valor.trim().toLowerCase();
        if (!emailFormatado.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new DominioException("Email em formato inválido");
        }
        this.valor = emailFormatado;
    }

    @Override
    public String toString() {
        return valor;
    }
}
