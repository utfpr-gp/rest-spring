package br.edu.utfpr.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import br.edu.utfpr.rest.model.service.StudentService;

@SpringBootApplication
public class RestSpringApplication {

    @Value("${pagina.quantidade}")
    private int quantidade;
    
    @Autowired
    private StudentService studentService;

    public static void main(String[] args) {
        SpringApplication.run(RestSpringApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            System.out.println("Quantidade " + quantidade);
            
            //inicializa a base de dados
            studentService.init();
        };
    }
}
