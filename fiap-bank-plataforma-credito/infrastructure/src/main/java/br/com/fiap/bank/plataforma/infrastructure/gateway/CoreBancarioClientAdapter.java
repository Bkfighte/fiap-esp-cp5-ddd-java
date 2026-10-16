package br.com.fiap.bank.plataforma.infrastructure.gateway;

import br.com.fiap.bank.plataforma.domain.gateway.CoreBancarioGateway;
import br.com.fiap.bank.plataforma.infrastructure.client.CoreBancarioFeignClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CoreBancarioClientAdapter implements CoreBancarioGateway {

    private static final Logger log = LoggerFactory.getLogger(CoreBancarioClientAdapter.class);

    private final CoreBancarioFeignClient coreBancarioFeignClient;

    @Override
    public Map<String, Object> consultarContaCliente(String cpf) {
        try {
            log.info("[OPENFEIGN CORE BANCÁRIO] Consultando conta para o CPF {}...", cpf);
            Map<String, Object> dadosConta = coreBancarioFeignClient.buscarContaPorCpf(cpf);
            if (dadosConta != null && !dadosConta.isEmpty()) {
                return dadosConta;
            }
        } catch (Exception e) {
            log.warn("[OPENFEIGN CORE BANCÁRIO] Serviço indisponível ou conta não localizada na porta 8080 ({}). Aplicando fallback padrão.", e.getMessage());
        }

        return Map.of(
                "cpf", cpf,
                "statusConta", "ATIVA",
                "saldo", new BigDecimal("12500.00"),
                "agencia", "0001",
                "numeroConta", "12345-6"
        );
    }
}
