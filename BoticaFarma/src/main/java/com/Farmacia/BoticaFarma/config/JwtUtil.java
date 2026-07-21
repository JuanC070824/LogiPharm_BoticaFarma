package com.Farmacia.BoticaFarma.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import com.Farmacia.BoticaFarma.model.Usuario;
import io.jsonwebtoken.Claims;
import java.util.function.Function;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET = "mi_clave_secreta_muy_larga_para_jwt_botica_farma_2024_segura";
    private final int EXPIRATION = 86400000; // 24 horas

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getUsername())
                .claim("rol", usuario.getRol().toString())
                .claim("nombre", usuario.getNombre())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractRol(String token) {
        return extractClaims(token).get("rol", String.class);
    }
    public Integer extractidBotica(String token) {
        Claims claims = extractClaims(token);
        return claims.get("idBotica", Integer.class);
    }

    public Integer extractidAlmacen(String token) {
        Claims claims = extractClaims(token);
        return claims.get("idAlmacen", Integer.class);
    }

    public Integer extractIdUsuario(String token) {
        Claims claims = extractClaims(token);
        return claims.get("idUsuario", Integer.class);
    }
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}