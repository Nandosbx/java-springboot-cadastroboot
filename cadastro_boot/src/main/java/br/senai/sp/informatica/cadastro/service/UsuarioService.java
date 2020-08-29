package br.senai.sp.informatica.cadastro.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import br.senai.sp.informatica.cadastro.model.Autorizacao;
import br.senai.sp.informatica.cadastro.model.Usuario;
import br.senai.sp.informatica.cadastro.repo.AutorizacaoRepo;
import br.senai.sp.informatica.cadastro.repo.UsuarioRepo;

@Service
public class UsuarioService {
	@Autowired
	private UsuarioRepo repo;
	@Autowired
	private AutorizacaoRepo autorizacaoRepo;
	
	
	//salvar
	public void salvar(Usuario usuario) {
		Usuario usuarioAntigo;
		
		
		//Houve mudança no nome?
		if(!usuario.getOld_nome().equalsIgnoreCase(usuario.getNome())) {
			//Localiza o registro do usuario com o nome antigo
			usuarioAntigo = getUsuario(usuario.getOld_nome());
			//Remove o registro do usuario com o nome antigo
			removeUsuario(usuario.getOld_nome());
		} else {
			//Se não houve mudança no nome, localize o registro dele
			usuarioAntigo = getUsuario(usuario.getNome());
		}
		
		if(usuario.isAdministrador()) {
			autorizacaoRepo.save(new Autorizacao(usuarioAntigo.getNome(),"ROLE_ADMIN"));
		} else {
			autorizacaoRepo.save(new Autorizacao(usuarioAntigo.getNome(),"ROLE_USER"));
		}
		
		if(usuarioAntigo != null) {
			usuario.setSenha(usuarioAntigo.getSenha());
		}
		repo.save(usuario);		
	}
	//listar
	public List<Usuario> getUsuarios(){
		return repo.findAll().stream()
				.map (u-> {
					Autorizacao autorizacao = 
							autorizacaoRepo.findById(u.getNome()).orElse(null);
					if(autorizacao != null) {
						u.setAdministrador(autorizacao.getPerfil().endsWith("ADMIN"));
					}
					return u;
				}) .collect(Collectors.toList());
	}
	//ler
	public Usuario getUsuario(String nome) {
		return repo.findById(nome).orElse(null);
		}
	
	public boolean removeUsuario(String nome) {
		Usuario usuario = getUsuario(nome);
		
		if(usuario != null) {
			Autorizacao autorizacao = autorizacaoRepo.findById(nome).orElse(null);
			if (autorizacao != null)
			{
				autorizacaoRepo.delete(autorizacao);
			}
			repo.delete(usuario);
			
			return true;
		} else {
			return false;
		}
	}
	public GrantedAuthority getAutorizacoes(String userId) {
		Autorizacao autorizacao = autorizacaoRepo.findById(userId).orElse(null);
				return autorizacao != null ? () -> autorizacao.getPerfil() : null;
				
	}
	
}
