package br.edu.utfpr.rest.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.BindingResult;

public class InvalidParamsException extends IllegalArgumentException {

	public InvalidParamsException() {
		super("Os parâmetros são inválidos. ");
	}
}
