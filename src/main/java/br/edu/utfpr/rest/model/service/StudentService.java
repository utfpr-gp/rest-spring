/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.rest.model.service;

import br.edu.utfpr.rest.model.entity.Student;
import br.edu.utfpr.rest.model.repository.StudentRepository;
import br.edu.utfpr.rest.util.CPFUtil;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ronifabio
 */
@Service
public class StudentService {

    public static final Logger log = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    StudentRepository studentRepository;

    public void init() {
        Student s1 = new Student(1L, "João de Sá Pato", "TSI", CPFUtil.geraCPF());
        Student s2 = new Student(2L, "José de Sá Pato", "TSI", CPFUtil.geraCPF());
        Student s3 = new Student(3L, "Júlio de Sá Pato", "TSI", CPFUtil.geraCPF());
        Student s4 = new Student(4L, "Josuel de Sá Pato", "TSI", CPFUtil.geraCPF());
        //Student s5 = new Student(5L, "Jô de Sá Pato", "TSI", "00000");
        Student s5 = new Student(5L, "Jô de Sá Pato", "TSI", CPFUtil.geraCPF());

        List<Student> students = Arrays.asList(s1, s2, s3, s4, s5);
        log.info("Inicializando o BD com {} objetos da classe {}", students.size(), Student.class.toString());
        studentRepository.saveAll(students);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Student findByCPF(String cpf) {
        log.info("Buscando pelo CPF {}", cpf);
        return studentRepository.findByCpf(cpf);
    }

    public Student save(Student student) throws DataIntegrityViolationException {
        log.info("Buscando o aluno: {}", student);
        return studentRepository.save(student);
    }

    public Student updateStudent(Long resgistration, String name, String course) {
        Student student = studentRepository.findById(resgistration).get();
        student.setName(name);
        student.setCourse(course);

        return student;
    }


}
