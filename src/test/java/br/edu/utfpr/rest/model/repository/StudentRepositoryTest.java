/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.rest.model.repository;

import br.edu.utfpr.rest.model.entity.Student;
import br.edu.utfpr.rest.util.CPFUtil;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.*;

import static org.junit.Assert.*;

import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author ronifabio
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class StudentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;

    /**
     *
     * Regra para verificar exceptions.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    String cpf = CPFUtil.geraCPF();

    public StudentRepositoryTest() {
    }

    @Before
    public void setUp() {
        Student s = new Student(7777L, "Steve Jobs", "TSI", cpf);
        studentRepository.save(s);
    }

    @After
    public void tearDown() {
        studentRepository.deleteAll();
    }

    @Test
    public void findByCpf() {
        Student s = studentRepository.findByCpf(cpf);
        assertNotNull(s);
        assertEquals(cpf, s.getCpf());
        Assertions.assertThat(s.getCpf()).isNotNull();
        Assertions.assertThat(s.getCpf()).isEqualTo(cpf);
    }

    @Test
    public void findByNameEndsWith() {
        List<Student> students = studentRepository.findByNameEndsWith("Jobs");
        assertFalse(students.isEmpty());
    }

    @Test
    public void findByNameEndsWithPageable() {
        Page<Student> page = studentRepository.findByNameEndsWith("Jobs", new PageRequest(1, 20));
        assertNotEquals(page.getTotalElements(), 0);
    }

    @Test
    public void findByCourse() {
        List<Student> students = studentRepository.findByCourse("TSI");
        assertFalse(students.isEmpty());
    }

    @Test
    public void delete() {
        Student student = new Student(111L, "Ronaldinho Gaúcho", "TSI", CPFUtil.geraCPF());
        this.studentRepository.save(student);
        student = studentRepository.getOne(student.getRegistration());
        long id = student.getRegistration();
        this.studentRepository.delete(student);
        //retorna Optional.empty
        Assertions.assertThat(studentRepository.findById(id)).isEmpty();
    }

    /**
     *
     * Exemplo de atualização de dados.
     *
     */
    @Test
    public void update() {
        Student student = new Student(111L, "Ronaldinho Gaúcho", "TSI", CPFUtil.geraCPF());
        this.studentRepository.save(student);
        student.setName("Ronaldo Gaúcho");
        student.setCourse("Engenharia Civil");

        //retorna o objeto salvo
        student = this.studentRepository.save(student);

        //forma mais correta é fazer uma nova busca pelo objeto
        Optional<Student> oStudent = studentRepository.findById(student.getRegistration());
        Assertions.assertThat(oStudent).isNotEmpty();
        Assertions.assertThat(oStudent.get().getName()).isEqualTo("Ronaldo Gaúcho");
        Assertions.assertThat(oStudent.get().getCourse()).isEqualTo("Engenharia Civil");
    }

    /**
     *
     * Tentativa de salvar dois estudantes com o mesmo CPF.
     * CPF é unique no Banco de dados.
     * Foi gerado DataIntegrityException pelo banco de dados.
     *
     */
    @Test
    public void dataIntegrityException(){
        thrown.expect(DataIntegrityViolationException.class);
        String cpf = CPFUtil.geraCPF();
        Student s1 = new Student(112L, "Ricardo Rocha", "TSI", cpf);
        Student s2 = new Student(113L, "Ricardo Gomes", "TSI", cpf);
        studentRepository.save(s1);
        studentRepository.save(s2);
    }
}
