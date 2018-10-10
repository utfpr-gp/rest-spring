package br.edu.utfpr.rest.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "disciplina")
@Data
public class Subject {

    @ManyToOne
    private Professor professor;

    public enum DayOfWeek {
        SEGUNDA, TERCA, QUARTA, QUINTA, SEXTA
    };

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sigla")
    private String acronym;

    @Column(name = "nome")
    private String name;

    @Column(name = "hora_inicio")
    private Integer startHour;

    @Column(name = "hora_fim")
    private Integer endHour;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_da_semana")
    private DayOfWeek dayOfWeek;

    public Subject() {
        super();
    }

    public Subject(Professor professor, String acronym, String name, Integer startHour, Integer endHour, DayOfWeek dayOfWeek) {
        this.professor = professor;
        this.acronym = acronym;
        this.name = name;
        this.startHour = startHour;
        this.endHour = endHour;
        this.dayOfWeek = dayOfWeek;
    }
}
