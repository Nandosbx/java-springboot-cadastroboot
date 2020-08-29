package br.senai.sp.informatica.cadastro.component;

import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;

/*
{ 
  "nome" : "O nome deve ter no mínimo 5 e no máximo 15 caracteres",
  "senha" : " A senha não tem a complexidade exigida"
  
 }
 */

public class JsonError {
	public static String build(BindingResult result) {
		return new StringBuilder("{\n" +
				result.getFieldErrors().stream()
					.map(erro -> "\"" + erro.getField() + "\" : \"" + erro.getDefaultMessage() + "\"")
					.collect(Collectors.joining(",\n")) + "\n}"
					).toString();	
		
	}
}
