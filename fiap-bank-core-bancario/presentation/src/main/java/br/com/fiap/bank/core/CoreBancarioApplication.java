package br.com.fiap.bank.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "br.com.fiap.bank.core")
public class CoreBancarioApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreBancarioApplication.class, args);
    }
}
