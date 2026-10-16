package br.com.fiap.bank.core.presentation.config;

import br.com.fiap.bank.core.application.dto.CriarClienteRequest;
import br.com.fiap.bank.core.application.dto.CriarContaRequest;
import br.com.fiap.bank.core.application.service.ClienteService;
import br.com.fiap.bank.core.application.service.ContaBancariaService;
import br.com.fiap.bank.core.domain.model.TipoConta;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CoreDataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CoreDataInitializer.class);

    private final ClienteService clienteService;
    private final ContaBancariaService contaBancariaService;

    @Override
    public void run(String... args) {
        if (!clienteService.listarTodos().isEmpty()) {
            return;
        }

        log.info("Populando base de dados inicial do Core Bancário (H2)...");

        clienteService.criar(new CriarClienteRequest(
                "12345678900",
                "Ana Silva",
                "ana.silva@fiapbank.com.br",
                LocalDate.of(1992, 5, 14),
                new BigDecimal("7500.00")
        ));

        contaBancariaService.abrirContaComNumero(new CriarContaRequest(
                "12345678900",
                "0001",
                TipoConta.CORRENTE,
                new BigDecimal("12500.00")
        ), "12345-6");

        clienteService.criar(new CriarClienteRequest(
                "98765432100",
                "Carlos Eduardo Oliveira",
                "carlos.oliveira@fiapbank.com.br",
                LocalDate.of(1985, 8, 22),
                new BigDecimal("12000.00")
        ));

        contaBancariaService.abrirContaComNumero(new CriarContaRequest(
                "98765432100",
                "0001",
                TipoConta.CORRENTE,
                new BigDecimal("45800.50")
        ), "78901-2");

        log.info("Carga inicial do Core Bancário concluída com 2 clientes e 2 contas ativas.");
    }
}
