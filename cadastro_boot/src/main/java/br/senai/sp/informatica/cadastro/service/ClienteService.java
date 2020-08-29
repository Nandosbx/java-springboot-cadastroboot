package br.senai.sp.informatica.cadastro.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.senai.sp.informatica.cadastro.model.Cliente;
import br.senai.sp.informatica.cadastro.repo.ClienteRepo;

@Service
public class ClienteService {
	@Autowired
	private ClienteRepo repo;
	
	public void salvar (Cliente cliente) {
		repo.save(cliente);
		
	}

	public List<Cliente> getClients(){
		return repo.findAll().stream()
				.filter(cliente -> !cliente.isDesativado())
				.collect(Collectors.toList());
		
	}
	
	public Cliente getCliente(int idCliente) {
		return repo.findById(idCliente).orElse(null);	//			
	}
	
	public void removeCliente(int[] lista) {
		Arrays.stream(lista).forEach(id -> {
			Cliente cliente = getCliente(id);
			
			if(cliente != null) {
				cliente.setDesativado(true);
				repo.save(cliente);
			}
		});
	}
	
}
