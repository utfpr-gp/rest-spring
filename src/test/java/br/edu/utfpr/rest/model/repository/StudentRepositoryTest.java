/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.rest.model.repository;

import br.edu.utfpr.rest.model.Student;
import br.edu.utfpr.rest.util.CPFUtil;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
}
