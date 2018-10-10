package br.edu.utfpr.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestSpringApplication {

    @Value("${pagina.quantidade}")
    private int quantidade;

    public static void main(String[] args) {
        SpringApplication.run(RestSpringApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            System.out.println("Quantidade " + quantidade);
        };
    }
}
