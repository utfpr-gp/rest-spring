package br.edu.utfpr.rest.exception;

import br.edu.utfpr.rest.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleException(EntityNotFoundException exception, HttpServletRequest request) {
        System.err.print("Error in process request: " + request.getRequestURL() + " cause by: "
                + exception.getClass().getSimpleName());

        Response response = new Response();
        response.addError("O dado solicitado não foi encontrado.");

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<?> handleException(EntityExistsException exception, HttpServletRequest request) {
        log.error("Error in process request: " + request.getRequestURL() + " cause by: "
                + exception.getClass().getSimpleName());

        Response response = new Response();
        response.addError(exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleException(DataIntegrityViolationException exception,
                                             HttpServletRequest request) {
        log.error("Error in process request: " + request.getRequestURL() + " cause by: "
                + exception.getClass().getSimpleName());
        Response response = new Response();
        response.addError(exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(InvalidParamsException.class)
    public ResponseEntity<?> handleException(InvalidParamsException exception, HttpServletRequest request) {
        log.error("Error in process request: " + request.getRequestURL() + " cause by: "
                + exception.getClass().getSimpleName());
        Response response = new Response();
        response.addError(exception.getMessage());

        // return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<?> handleException(EmptyResultDataAccessException exception,
                                             HttpServletRequest request) {
        log.error("Error in process request: " + request.getRequestURL() + " cause by: "
                + exception.getClass().getSimpleName());
        Response response = new Response();
        response.addError(exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.GONE);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleException(ConstraintViolationException exception,
                                             HttpServletRequest request) {
        log.error("Error in process request: " + request.getRequestURL() + " cause by: "
                + exception.getClass().getSimpleName());
        Response response = new Response();
        response.addError(exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleException(NoHandlerFoundException exception,
                                             HttpServletRequest request) {
        log.error("Error in process request: " + request.getRequestURL() + " cause by: "
                + exception.getClass().getSimpleName());
        Response response = new Response();
        response.addError("Página não encontrada");

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
