package br.senai.sp.informatica.cadastro.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senai.sp.informatica.cadastro.model.Cliente;

public interface ClienteRepo extends JpaRepository<Cliente, Integer>{

}
