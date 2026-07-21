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
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();

        // Ignorar filtrado de JWT para solicitudes OPTIONS (Preflight CORS)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        return pathMatcher.match("/api/auth/**", path) ||
                pathMatcher.match("/boticafarma/login", path) ||
                pathMatcher.match("/boticafarma/auth/**", path);
    }

    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            if (jwtUtil.isTokenExpired(token)) {
                System.err.println("⚠️ Token expirado enviado en la petición.");
                filterChain.doFilter(request, response);
                return;
            }

            String username = jwtUtil.extractUsername(token);
            String rol = jwtUtil.extractRol(token);

            // EXTRAER ATRIBUTOS MULTI-TENANT DEL TOKEN Y PONERLOS EN EL REQUEST
            Integer idBotica = jwtUtil.extractClaim(token, claims -> claims.get("idBotica", Integer.class));
            Integer idAlmacen = jwtUtil.extractClaim(token, claims -> claims.get("idAlmacen", Integer.class));

            if (idBotica != null) {
                request.setAttribute("idBotica", idBotica);
            }
            if (idAlmacen != null) {
                request.setAttribute("idAlmacen", idAlmacen);
            }
            request.setAttribute("rol", rol);

            // 👇 CORRECCIÓN 1: Evitar duplicar el prefijo ROLE_
            String authorityName = (rol != null && rol.startsWith("ROLE_")) ? rol : "ROLE_" + rol;
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(authorityName);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            Collections.singletonList(authority)
                    );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Establecer el usuario autenticado en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            // 👇 CORRECCIÓN 2: Mostrar el stacktrace completo si falla
            System.err.println("❌ Error al validar token en JwtAuthenticationFilter: " + e.getMessage());
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }
}