package br.senai.sp.informatica.cadastro.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import br.senai.sp.informatica.cadastro.model.validacao.Senha;
import lombok.Data;

@Data
@Entity
@Table(name="user")
public class Usuario {
	@Id		
	@Column(name="username", length = 15)
	@Size(min=5, max=15, message="O nome deve ter no mínimo 5 e máximo 15 caracteres")
	private String nome;
	
	//	
	@Transient
	private String old_nome = "";
	
	//SENHA
	@Column(name="password")	
	@Senha(message="A senha não tem a complexidade exigida")
	private String senha;
	
	@Transient
	private boolean Administrador;
	
	@Column(name="enabled")
	private boolean Habilitado;

	
}