package br.senai.sp.informatica.cadastro.model.validacao;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SenhaValidator implements ConstraintValidator<Senha, String> {

	//Declarando uma função
	/*
	 * 
	 * */
	private BiFunction<String, Predicate<Integer>,Boolean>	regra =
			(texto, condicao) -> texto.chars() //.chars análisa cada carácter no texto
					.filter(c-> condicao.test(c))
					.findAny()
					.isPresent();
	
	
	/*
	 *  Validação da Senha utilizando as seguintes regras:
	 *  
	 *   - pelo menos um carácter especial (#, &, $, %)
	 *   - pelo menos uma letra maiúscula
	 *   - conter números
	 *   - ter o tamanho de 8 caracteres
	 */	
	 
	@Override
	public boolean isValid(String senha, ConstraintValidatorContext context) {
		return !(senha.length() < 8) && // a senha não pode ter o tamanho menor que 8
				regra.apply(senha, c -> c =='#' || c =='&' || c =='$' || c =='%') &&
				regra.apply(senha, Character::isUpperCase) &&
				regra.apply(senha, Character::isDigit);
		
		
	}

		
	
		}
