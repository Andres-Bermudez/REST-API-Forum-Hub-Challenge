package com.alura.restapiforohubchallenge.security;

import java.io.IOException;

import com.alura.restapiforohubchallenge.domain.login.jsonwebtoken.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import com.alura.restapiforohubchallenge.domain.login.user.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String tokenReceivedHeader = request.getHeader("Authorization");

        // Primero se verifica si el encabezado no es null y si comienza con "Bearer ". Esto asegura que
        // se está tratando con un token JWT correctamente formado.
        if (tokenReceivedHeader != null && tokenReceivedHeader.startsWith("Bearer ")) {
            // Eliminar la parte "Bearer " del encabezado para obtener solo el token.
            String token = tokenReceivedHeader.replace("Bearer ", "").trim();

            // Valida el token
            if (tokenService.validateToken(token)) {
                // Obtener el sujeto del token (normalmente el nombre de usuario).
                String username = tokenService.getSubjectToken(token);

                // Verifica que el nombre de usuario no sea null y que no haya una
                // autenticación ya presente en el contexto de seguridad.
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Buscar el usuario.
                    UserDetails userDetails = userRepository.findByUserName(username);

                    // Verifica si el usuario existe.
                    if (userDetails != null) {
                        // Crea un objeto de autenticación.
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()); // Forzar el inicio de sesion.

                        // Establece la autenticación en el contexto de seguridad.
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } else {
                // Si el token no es válido, retorna una respuesta de "Unauthorized".
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Unauthorized: Invalid token.");
                return;
            }
        }
        // Continua con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
