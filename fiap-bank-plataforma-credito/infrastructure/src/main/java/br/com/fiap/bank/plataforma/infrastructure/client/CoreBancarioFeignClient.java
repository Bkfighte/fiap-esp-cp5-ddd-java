package br.com.fiap.bank.plataforma.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "core-bancario-client", url = "${fiap.bank.core-bancario.url:http://localhost:8080}")
public interface CoreBancarioFeignClient {

    @GetMapping("/api/v1/contas/cliente/{cpf}")
    Map<String, Object> buscarContaPorCpf(@PathVariable("cpf") String cpf);
}
