package br.senai.sp.informatica.cadastro.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.senai.sp.informatica.cadastro.component.JsonError;
import br.senai.sp.informatica.cadastro.model.Cliente;
import br.senai.sp.informatica.cadastro.model.Servico;
import br.senai.sp.informatica.cadastro.model.valueObject.ListaDeServicos;
import br.senai.sp.informatica.cadastro.service.ClienteService;
import br.senai.sp.informatica.cadastro.service.ServicoService;

@RestController
@RequestMapping("/api")
public class ClienteController {
	
	@Autowired
	private ClienteService clienteService;
	@Autowired
	private ServicoService servicoService;
	
	
	@PostMapping("/cadastra") 
	public ResponseEntity<Object> cadastra(@RequestBody @Valid Cliente cliente, BindingResult result){
		
	if(result.hasErrors()) {
			return ResponseEntity.unprocessableEntity()
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.body(JsonError.build(result));
		} else {
	clienteService.salvar(cliente);
	return ResponseEntity.ok().build();		
	}	
	}
	@RequestMapping("/listaCliente")
	public ResponseEntity<List<Cliente>> listaCliente() {
	return ResponseEntity.ok(clienteService.getClients());		
	}
	
	@RequestMapping("/editaCliente/{idCliente}")
	public ResponseEntity<Object> editaCliente(@PathVariable("idCliente") int idCliente){
		Cliente cliente = clienteService.getCliente(idCliente);
		
		if(cliente != null) {
			return ResponseEntity.ok(cliente);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/removeCliente")
	public ResponseEntity<Object> removeCliente(@RequestBody int [] lista){
		clienteService.removeCliente(lista);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping("/carregaServicos/{idCliente}")
	public ResponseEntity<Object> carregaServicos(@PathVariable("idCliente") int idCliente){
		Cliente cliente = clienteService.getCliente(idCliente);
		
		if (cliente != null) {
			return ResponseEntity.ok(servicoService.getServicos(cliente));
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/selecionaServico")
	public ResponseEntity<Object> selecionaServico(@RequestBody ListaDeServicos lista){
		//Localizar o cliente pelo ID
		Cliente cliente = clienteService.getCliente(lista.getIdCliente());
		
		//Se o cliente foi encontrado
		if(cliente != null) {
			// Garantir que a lista de serviços não esteja nula
			if(cliente.getServicos() == null)
				cliente.setServicos(new ArrayList<>());
			
			
			//Declara um Teste (if) para verificar se um determinado serviço
			//está contido na lista dos serviços enviados pelo Front-End
			Predicate<Servico> naoEstaNaLista = (srvDoCliente) -> !Arrays.stream(lista.getServicos())
					.filter (srvDaLista -> srvDaLista.getIdServico() == srvDoCliente.getIdServico())
					.findFirst().get().isSelecionado();
			
			// Criar uma lista com IDs de serviços a serem excluídos do Cliente
			List<Servico> aDeleter = cliente.getServicos().stream()
					.filter(naoEstaNaLista)
					.collect(Collectors.toList());
			
			//Excluir os Serviços do Cliente
			aDeleter.parallelStream().forEach(servico -> cliente.getServicos()
					.removeIf(srv -> srv.getIdServico() == servico.getIdServico()));
			
			//Incluir os serviços para o cliente
			Arrays.stream(lista.getServicos()).filter(Servico::isSelecionado)//Redução de uma expressão lambda "::" = servico -> 
				.filter(servico -> !cliente.getServicos().contains(servico))
				.forEach(servico -> cliente.getServicos().add(servico));
			
				clienteService.salvar(cliente);
				
				return ResponseEntity.ok().build();
				
			} else {
				
				return ResponseEntity.unprocessableEntity().build();
		}
	
}
}
