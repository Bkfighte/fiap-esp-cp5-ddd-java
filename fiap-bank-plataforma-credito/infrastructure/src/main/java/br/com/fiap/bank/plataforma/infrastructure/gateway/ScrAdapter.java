package br.com.fiap.bank.plataforma.infrastructure.gateway;

import br.com.fiap.bank.plataforma.domain.gateway.ScrGateway;
import org.springframework.stereotype.Component;

@Component
public class ScrAdapter implements ScrGateway {

    @Override
    public boolean possuiRestricoesBacen(String cpf) {
        if (cpf == null) {
            return true;
        }
        String digits = cpf.replaceAll("\\D", "");
        return digits.endsWith("99") || digits.startsWith("000");
    }
}
