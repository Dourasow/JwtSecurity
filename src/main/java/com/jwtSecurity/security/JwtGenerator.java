package com.jwtSecurity.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtGenerator {

    public String generateToken(Authentication authentication)
    {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expiredDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.JWTSECRET)
                .compact();

        return token;
    }

    public String getUsernameFromJwt(String token)
    {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.JWTSECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validToken(String token)
    {
        try
        {
            Jwts.parser().setSigningKey(SecurityConstants.JWTSECRET).parseClaimsJws(token);
            return true;
        }catch (Exception e)
        {
            throw new AuthenticationCredentialsNotFoundException("JWT is expired or incorrect");
        }
    }
}
