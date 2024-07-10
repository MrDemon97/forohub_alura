package com.alura.forohub.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${spring.security.jwt.secret}")
    private String secreto;

    @Value("${spring.security.jwt.expiration}")
    private Long expiracion;

    public String extraerEmail(String token){
        return extraerReclamo(token, Claims::getSubject);
    }

    public <T> T extraerReclamo(String token, Function<Claims, T> reclamoResolver){
        final Claims reclamos = extraerTodosLosReclamos(token);
        return reclamoResolver.apply(reclamos);
    }

    private Claims extraerTodosLosReclamos(String token){
        return Jwts.parser().setSigningKey(secreto).build()
                .parseSignedClaims(token).getBody();
    }

    public String generarToken(UserDetails userDetails){
        Map<String, Object> reclamos = new HashMap<>();
        return crearToken(reclamos, userDetails.getUsername());
    }

    private String crearToken(Map<String,Object> reclamos, String subjet){
        return Jwts.builder().setClaims(reclamos).setSubject(subjet).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiracion))
                .signWith(SignatureAlgorithm.HS256,secreto).compact();
    }

    public Boolean validarToken(String token,UserDetails userDetails){
        final String username = extraerEmail(token);
        return (username.equals(userDetails.getUsername()) && !estaExpirado(token));
    }

    public Boolean estaExpirado(String token){
        return extraerFechaExpiracion(token).before(new Date());
    }

    public Date extraerFechaExpiracion(String token){
        return extraerReclamo(token, Claims::getExpiration);
    }


}
