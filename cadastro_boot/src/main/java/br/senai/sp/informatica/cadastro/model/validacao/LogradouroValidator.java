package br.senai.sp.informatica.cadastro.model.validacao;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LogradouroValidator implements ConstraintValidator<Logradouro, String> {

	private Logradouro anotacao;
	
	//Método
	
	//Obtém a referência da anotação
	@Override
	public void initialize(Logradouro constraintAnnotation) {
		anotacao = constraintAnnotation;
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		int max = anotacao.max();
		
		if(value ==  null || value.length() > max) {		
		return false;
		} else {
			String[] texto = value.split(" ");
			
			if(
					(
					texto[0].equalsIgnoreCase("rua") ||
					texto[0].equalsIgnoreCase("av.") ||
					texto[0].equalsIgnoreCase("praça") ||
					texto[0].equalsIgnoreCase("alameda") ||
					texto[0].equalsIgnoreCase("estr.") ||
					texto[0].equalsIgnoreCase("estrada") ||
					texto[0].equalsIgnoreCase("beco")) &&
					texto.length > 1)				
					{
						return true;
			} else {
						return false;
			}
		}
	}
}
