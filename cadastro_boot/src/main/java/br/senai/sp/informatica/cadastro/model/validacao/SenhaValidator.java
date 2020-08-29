package br.senai.sp.informatica.cadastro.model.validacao;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SenhaValidator implements ConstraintValidator<Senha, String> {

	//Declarando uma fun��o
	/*
	 * 
	 * */
	private BiFunction<String, Predicate<Integer>,Boolean>	regra =
			(texto, condicao) -> texto.chars() //.chars an�lisa cada car�cter no texto
					.filter(c-> condicao.test(c))
					.findAny()
					.isPresent();
	
	
	/*
	 *  Valida��o da Senha utilizando as seguintes regras:
	 *  
	 *   - pelo menos um car�cter especial (#, &, $, %)
	 *   - pelo menos uma letra mai�scula
	 *   - conter n�meros
	 *   - ter o tamanho de 8 caracteres
	 */	
	 
	@Override
	public boolean isValid(String senha, ConstraintValidatorContext context) {
		return !(senha.length() < 8) && // a senha n�o pode ter o tamanho menor que 8
				regra.apply(senha, c -> c =='#' || c =='&' || c =='$' || c =='%') &&
				regra.apply(senha, Character::isUpperCase) &&
				regra.apply(senha, Character::isDigit);
		
		
	}

		
	
		}
