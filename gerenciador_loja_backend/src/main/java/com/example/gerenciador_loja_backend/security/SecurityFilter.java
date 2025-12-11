package com.example.gerenciador_loja_backend.security;

import com.example.gerenciador_loja_backend.models.Usuario;
import com.example.gerenciador_loja_backend.repositories.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = recoverToken(request);

            if (token != null) {
                String username = tokenService.validateToken(token); // retorna username do token

                if (username != null) {
                    userRepository.findByUsername(username).ifPresent(user -> {
                        var authorities = Collections.singletonList(
                                new SimpleGrantedAuthority("ROLE_USER")
                        );

                        var authentication = new UsernamePasswordAuthenticationToken(
                                user, null, authorities
                        );

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    });
                }
            }
        } catch (Exception e) {
            // Token inválido ou erro → limpa autenticação
            SecurityContextHolder.clearContext();
        }

        // continua o fluxo de filtros
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        return authHeader.substring(7); // remove "Bearer "
    }
}
