package br.com.fiap.bank.plataforma.presentation.config;

import br.com.fiap.bank.plataforma.application.dto.SolicitarCreditoRequest;
import br.com.fiap.bank.plataforma.application.service.PlataformaCreditoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PlataformaDataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(PlataformaDataInitializer.class);

    private final PlataformaCreditoService service;

    @Override
    public void run(String... args) {
        if (!service.possuiSolicitacoesCadastradas()) {
            log.info("Populando base H2 da Plataforma de Crédito com solicitações iniciais de teste...");

            service.processarNovaSolicitacao(new SolicitarCreditoRequest(
                    "12345678900",
                    "Ana Silva",
                    "EMPRÉSTIMO PESSOAL",
                    new BigDecimal("18000.00"),
                    24,
                    new BigDecimal("7500.00")
            ));

            service.processarNovaSolicitacao(new SolicitarCreditoRequest(
                    "55566677788",
                    "Carlos Eduardo Oliveira",
                    "CRÉDITO IMOBILIÁRIO",
                    new BigDecimal("240000.00"),
                    180,
                    new BigDecimal("12000.00")
            ));

            service.processarNovaSolicitacao(new SolicitarCreditoRequest(
                    "99988877799",
                    "Roberto Almeida",
                    "FINANCIAMENTO DE VEÍCULO",
                    new BigDecimal("65000.00"),
                    48,
                    new BigDecimal("4200.00")
            ));

            log.info("Carga inicial da Plataforma de Crédito concluída com sucesso!");
        }
    }
}
