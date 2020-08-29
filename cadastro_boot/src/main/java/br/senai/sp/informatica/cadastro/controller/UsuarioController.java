package br.senai.sp.informatica.cadastro.controller;

import java.util.List;

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
import br.senai.sp.informatica.cadastro.model.Usuario;
import br.senai.sp.informatica.cadastro.service.UsuarioService;

@RestController
@RequestMapping("/api")
public class UsuarioController {
	@Autowired
	private UsuarioService usuarioService;
	
	@PostMapping("/salvaUsuario")
	public ResponseEntity<Object> salvaUsuario(@RequestBody @Valid Usuario usuario, BindingResult result){
		
		if(result.hasErrors()) {
			return ResponseEntity.unprocessableEntity()
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.body(JsonError.build(result));
		} else {		
		
		usuarioService.salvar(usuario);
		return ResponseEntity.ok().build();
		}
	}
	
	@RequestMapping("/listaUsuario")
	public ResponseEntity<List<Usuario>> listaUsuario() {
		return ResponseEntity.ok(usuarioService.getUsuarios());		
	}
	
	@RequestMapping("/editaUsuario/{nome}")
	public ResponseEntity<Object> editaUsuario(@PathVariable("nome") String nome){
		Usuario usuario = usuarioService.getUsuario(nome);
		
		if(usuario != null) {
			usuario.setOld_nome(usuario.getNome());
			return ResponseEntity.ok(usuario);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
		
	@RequestMapping("/removeUsuario/{nome}")
	public ResponseEntity<Object> removeUsuario(@PathVariable("nome") String nome){
		if(usuarioService.removeUsuario(nome)) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.unprocessableEntity().build();
		}
	}
}
