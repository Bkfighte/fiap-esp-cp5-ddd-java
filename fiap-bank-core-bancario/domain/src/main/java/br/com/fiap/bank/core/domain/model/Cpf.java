package br.com.fiap.bank.core.domain.model;

import br.com.fiap.bank.core.domain.exception.DominioException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Cpf {

    private final String valor;

    public Cpf(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new DominioException("CPF é obrigatório");
        }
        // limpa mascara caso venha com ponto ou traco
        String cpfLimpo = valor.replaceAll("\\D", "");
        if (!cpfLimpo.matches("\\d{11}")) {
            throw new DominioException("CPF inválido, deve ter 11 números");
        }
        this.valor = cpfLimpo;
    }

    @Override
    public String toString() {
        return valor;
    }
}
