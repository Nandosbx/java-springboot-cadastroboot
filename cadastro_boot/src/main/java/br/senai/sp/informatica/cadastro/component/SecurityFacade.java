package br.senai.sp.informatica.cadastro.component;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.senai.sp.informatica.cadastro.model.Usuario;

@Component
public class SecurityFacade {
	public String getUserName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null) {
			Usuario usuario = (Usuario)auth.getPrincipal();
			return usuario.getNome();
		}
		return null;
	}
}

