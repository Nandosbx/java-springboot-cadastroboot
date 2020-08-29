package br.senai.sp.informatica.cadastro.model.validacao;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy=SenhaValidator.class)
public @interface Senha {
	String message() default "A senha n�o tem a complexidade exigida";
	
	//Espec�ficos de anota��o
	//Payload � a carga que transporta, aquilo que transporta, as coisas transportadas
	//A informa��o pr�pria
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default{};

}