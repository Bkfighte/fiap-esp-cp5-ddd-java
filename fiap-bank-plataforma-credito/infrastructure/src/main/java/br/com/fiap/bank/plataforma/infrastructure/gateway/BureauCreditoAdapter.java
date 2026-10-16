package br.com.fiap.bank.plataforma.infrastructure.gateway;

import br.com.fiap.bank.plataforma.domain.gateway.BureauCreditoGateway;
import org.springframework.stereotype.Component;

@Component
public class BureauCreditoAdapter implements BureauCreditoGateway {

    @Override
    public int consultarScore(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            return 350;
        }
        String digits = cpf.replaceAll("\\D", "");
        if (digits.endsWith("00")) {
            return 820;
        }
        if (digits.endsWith("99")) {
            return 320;
        }
        if (digits.endsWith("55")) {
            return 540;
        }
        return 680;
    }
}
