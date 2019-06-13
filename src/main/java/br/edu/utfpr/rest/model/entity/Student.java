package br.edu.utfpr.rest.model.entity;

import br.edu.utfpr.rest.model.dto.StudentDTO;
import java.util.Date;
import java.util.Set;
import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import lombok.Data;

import org.hibernate.validator.constraints.br.CPF;

@Entity
@Table(name = "students")
@Data
public class Student {

    @Id
    @Column(name = "id")
    private Long registration;

    @Column
    private String name;

    @Column
    private String course;

    @Column(updatable = false, unique = true)
    private String cpf;

    private Date created;
    private Date updated;

    @ManyToMany
    private Set<Subject> subjects;

    public Student() {
        super();
    }

    public Student(Long registration, String name, String course, String cpf) {
        this.registration = registration;
        this.name = name;
        this.course = course;
        this.cpf = cpf;
    }

    public Student(StudentDTO dto) {
        this.registration = dto.getRegistration();
        this.name = dto.getName();
        this.course = dto.getCourse();
        this.cpf = dto.getCpf();
    }

    public void update(StudentDTO dto) {
        this.name = dto.getName();
        this.course = dto.getCourse();
        this.cpf = dto.getCpf();
    }

    @PreUpdate
    public void onUpdate() {
        this.updated = new Date();
    }

    @PrePersist
    public void onSave() {
        final Date now = new Date();
        this.created = now;
        this.updated = now;
    }

    @Override
    public String toString() {
        return "Student{" + "registration=" + registration + ", name=" + name + ", course=" + course + ", cpf=" + cpf + '}';
    }

}
