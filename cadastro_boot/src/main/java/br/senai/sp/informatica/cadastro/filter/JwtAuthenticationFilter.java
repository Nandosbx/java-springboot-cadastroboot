package br.senai.sp.informatica.cadastro.filter;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import br.senai.sp.informatica.cadastro.component.JwtTokenProvider;
import br.senai.sp.informatica.cadastro.model.Usuario;
import br.senai.sp.informatica.cadastro.service.UsuarioService;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
	@Autowired
	private JwtTokenProvider tokenProvider;
	@Autowired
	private UsuarioService usuarioService;
	
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			// Obtém o token da requisição Web
			String jwt = getJwtFromRequest(request);
			
			logger.error("JWT: " + jwt);
			
			// Verifica se achou o token e que ele é válido
			if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
				// Obtém o userId do Token
				String userId = tokenProvider.getUserIdFromJWT(jwt);
				// Localiza no Banco de Dados o registro do Usuário a partir do userId
				Usuario usuario = usuarioService.getUsuario(userId);
				
				// Constroi a credencial de autenticação com os dados do Usuário, e a lista de Perfis
				// aos quais o usuário pertence
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
					usuario, null, Collections.singletonList(usuarioService.getAutorizacoes(userId))
				);
				
				// Associa à credencial de autenticação a requisição Web
				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				// Autentica o usuário com a credencial de Autenticação
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		} catch (Exception ex) {
			logger.error("Não foi possível configurar a segurança", ex);
		}
		
		filterChain.doFilter(request, response);  /// Nunca esqueçam desta Linha ****
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		// Extrai o Token JWT do cabeçalho HTTP 
		String token = request.getHeader("Authorization");
		
		// Verifica se o Token JWT existe e é reconhecido
		if(StringUtils.hasText(token) && token.startsWith("Bearer")) {
			// Retorna o token sem seu prefixo "Bearer"
			return token.substring(7, token.length());
		}
		
		return null;
	}
}
