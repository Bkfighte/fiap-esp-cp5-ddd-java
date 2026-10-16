package br.com.fiap.bank.plataforma.domain.gateway;

public interface NotificacaoGateway {

    void notificarCliente(String cpf, String status, String parecer);
}
