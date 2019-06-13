package br.edu.utfpr.rest.model.dto;

import br.edu.utfpr.rest.model.entity.Student;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

@Data
@NoArgsConstructor
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
}
