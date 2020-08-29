package br.senai.sp.informatica.cadastro.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senai.sp.informatica.cadastro.model.Usuario;

public interface UsuarioRepo extends JpaRepository<Usuario, String>{

}