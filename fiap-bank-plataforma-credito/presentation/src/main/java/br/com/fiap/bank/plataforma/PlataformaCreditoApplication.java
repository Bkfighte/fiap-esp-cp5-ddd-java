package br.com.fiap.bank.plataforma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "br.com.fiap.bank.plataforma")
public class PlataformaCreditoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlataformaCreditoApplication.class, args);
    }
}
