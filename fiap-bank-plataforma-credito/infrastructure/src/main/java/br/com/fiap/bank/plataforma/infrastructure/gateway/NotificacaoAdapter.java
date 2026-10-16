package br.com.fiap.bank.plataforma.infrastructure.gateway;

import br.com.fiap.bank.plataforma.domain.gateway.NotificacaoGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoAdapter implements NotificacaoGateway {

    private static final Logger log = LoggerFactory.getLogger(NotificacaoAdapter.class);

    @Override
    public void notificarCliente(String cpf, String status, String parecer) {
        log.info("[SERVIÇO DE NOTIFICAÇÃO] Enviando Push/E-mail para CPF: {} | Status: {} | Mensagem: {}", cpf, status, parecer);
    }
}
