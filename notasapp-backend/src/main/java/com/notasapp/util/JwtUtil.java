package com.notasapp.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilidad para generar y validar tokens JWT.
 */
@Component
public class JwtUtil {

    // Clave secreta para firmar los tokens (se regenera al reiniciar la app)
    private final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    // Duracion del token sin "mantener sesion": 24 horas
    private static final long EXPIRATION_TIME_SHORT = 1000 * 60 * 60 * 24;

    // Duracion del token con "mantener sesion": 30 dias
    private static final long EXPIRATION_TIME_LONG = 1000L * 60 * 60 * 24 * 30;

    /**
     * GENERAR UN TOKEN JWT
     */
    public String generarToken(String userId, String username, boolean mantenerSesion) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);

        long expirationTime = mantenerSesion ? EXPIRATION_TIME_LONG : EXPIRATION_TIME_SHORT;

        return Jwts.builder()
                .claims(claims)
                .subject(userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * EXTRAER EL USER ID DEL TOKEN
     */
    public String extraerUserId(String token) {
        return extraerClaims(token).getSubject();
    }

    /**
     * EXTRAER EL USERNAME DEL TOKEN
     */
    public String extraerUsername(String token) {
        return (String) extraerClaims(token).get("username");
    }

    /**
     * EXTRAER LA FECHA DE EXPIRACION DEL TOKEN
     */
    public Date extraerExpiracion(String token) {
        return extraerClaims(token).getExpiration();
    }

    /**
     * EXTRAER TODOS LOS CLAIMS (DATOS) DEL TOKEN
     */
    private Claims extraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * COMPROBAR SI EL TOKEN HA EXPIRADO
     */
    public boolean esTokenExpirado(String token) {
        try {
            return extraerExpiracion(token).before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * VALIDAR TOKEN CON USER ID
     */
    public boolean validarToken(String token, String userId) {
        try {
            final String tokenUserId = extraerUserId(token);
            return (tokenUserId.equals(userId) && !esTokenExpirado(token));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * VALIDAR TOKEN (sin userId)
     */
    public boolean validarToken(String token) {
        try {
            extraerClaims(token);
            return !esTokenExpirado(token);
        } catch (Exception e) {
            return false;
        }
    }
}
