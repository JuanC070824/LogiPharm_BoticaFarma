package com.Farmacia.BoticaFarma.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Extraer el token del header Authorization
        String authHeader = request.getHeader("Authorization");
        System.out.println("🔍 REQUEST: " + request.getRequestURI());
        System.out.println("🔑 AUTH HEADER: " + authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ No hay token o no empieza con Bearer");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); // Remover "Bearer "

        try {
            // 2. Validar el token
            if (jwtUtil.isTokenExpired(token)) {
                System.out.println("❌ Token expirado");
                filterChain.doFilter(request, response);
                return;
            }

            // 3. Extraer información del token
            String username = jwtUtil.extractUsername(token);
            String rol = jwtUtil.extractRol(token);

            // 🔍 AGREGAR ESTOS LOGS:
            System.out.println("✅ Token válido");
            System.out.println("👤 Username: " + username);
            System.out.println("🎭 Rol extraído: " + rol);
            System.out.println("🎭 Rol con prefijo: ROLE_" + rol);

            // 4. Crear la autenticación
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + rol);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            Collections.singletonList(authority)
                    );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 5. Establecer la autenticación en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authentication);

            System.out.println("✅ Autenticación establecida correctamente");

        } catch (Exception e) {
            System.err.println("❌ Error al validar token: " + e.getMessage());
            e.printStackTrace(); // AGREGAR esto para ver el stack trace completo
        }

        // 6. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}