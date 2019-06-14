package br.edu.utfpr.rest.controller;

import br.edu.utfpr.rest.model.dto.StudentDTO;
import br.edu.utfpr.rest.model.entity.Student;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class StudentDTOValidationTest {

    private Validator validator;

    @Before
    public void setUp(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validEmptyStudent(){
        StudentDTO student = new StudentDTO();
        Set<ConstraintViolation<StudentDTO>> violations = validator.validate(student);
        Assert.assertFalse(violations.isEmpty());
    }
}
