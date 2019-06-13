/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.rest.model.repository;

import br.edu.utfpr.rest.model.entity.Student;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ronifabio
 */
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Transactional(readOnly = true)
    Student findByCpf(String cpf);

    @Query("select s from Student s where s.name like %?1")
    List<Student> findByNameEndsWith(String name);

    @Query("select s from Student s where s.name like %:name")
    Page<Student> findByNameEndsWith(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT * FROM alunos WHERE curso = ?1", nativeQuery = true)
    List<Student> findByCourse(String course);
}
