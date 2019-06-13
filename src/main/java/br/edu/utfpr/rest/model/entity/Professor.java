package br.edu.utfpr.rest.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "professor")
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Professor {

    @Id
    @Column(name = "id")
    private Long registration;

    @Column
    private String name;

    @Column
    private String departament;

    @ManyToOne
    private University university;

    public Professor(Long registration, String name, String departament, University university) {
        this.registration = registration;
        this.name = name;
        this.departament = departament;
        this.university = university;
    }

}
