package br.senai.sp.informatica.cadastro.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senai.sp.informatica.cadastro.model.Autorizacao;

public interface AutorizacaoRepo extends JpaRepository <Autorizacao, String>{

}
