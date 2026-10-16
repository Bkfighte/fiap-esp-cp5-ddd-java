package br.com.fiap.bank.plataforma.infrastructure.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "br.com.fiap.bank.plataforma.infrastructure.client")
public class FeignInfrastructureConfig {
}
