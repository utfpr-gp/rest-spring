package br.edu.utfpr.rest.model.dto;

import br.edu.utfpr.rest.model.Student;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.validation.BindingResult;

public class StudentDTO {

    private Long registration;

    @NotEmpty(message = "O nome não pode ser vazio")
    @Pattern(regexp = "^(\\s?[A-ZÀ-Ú][a-zà-ú]*)+(\\s[a-zà-ú]*)?(\\s[A-ZÀ-Ú][a-zà-ú]*)+",
            message = "Insira o seu nome completo iniciando com letras maíusculas.")
    private String name;

    @NotEmpty(message = "O nome do curso não pode ser vazio.")
    @Length(min = 2, max = 100, message = "O nome do curso de conter no mínimo 2 e máximo 100 caracteres.")
    private String course;

    @CPF(message = "Insira um número de CPF válido.")
    private String cpf;

    public StudentDTO() {
    }

    public StudentDTO(Long registration, String name, String course, String cpf) {
        this.registration = registration;
        this.name = name;
        this.course = course;
        this.cpf = cpf;
    }

    public StudentDTO(Student student) {
        this.registration = student.getRegistration();
        this.name = student.getName();
        this.course = student.getCourse();
        this.cpf = student.getCpf();
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Long getRegistration() {
        return registration;
    }

    public void setRegistration(Long registration) {
        this.registration = registration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

}
