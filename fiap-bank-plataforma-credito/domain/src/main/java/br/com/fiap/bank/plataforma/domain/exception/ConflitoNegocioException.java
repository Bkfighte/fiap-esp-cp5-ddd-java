package br.com.fiap.bank.plataforma.domain.exception;

public class ConflitoNegocioException extends RuntimeException {

    public ConflitoNegocioException(String message) {
        super(message);
    }
}
