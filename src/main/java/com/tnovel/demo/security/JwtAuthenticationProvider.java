package com.tnovel.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;

@Slf4j
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    public static final String AUTHORITIES_KEY = "roles";

    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(((JwtAuthenticationToken) authentication).getToken())
                    .getPayload();
        } catch (SignatureException signatureException) {
            String error = "signature key is different";
            log.error(error, signatureException);
            throw new JwtInvalidException(error, signatureException);
        } catch (ExpiredJwtException expiredJwtException) {
            String error = "expired token";
            log.error(error, expiredJwtException);
            throw new JwtInvalidException(error, expiredJwtException);
        } catch (MalformedJwtException malformedJwtException) {
            String error = "malformed token";
            log.error(error, malformedJwtException);
            throw new JwtInvalidException(error, malformedJwtException);
        } catch (IllegalArgumentException illegalArgumentException) {
            String error = "using illegal argument like null";
            log.error(error, illegalArgumentException);
            throw new JwtInvalidException(error, illegalArgumentException);
        }
        return new JwtAuthenticationToken(claims.getSubject(), getAuthoritiesFromToken(claims));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Collection<? extends GrantedAuthority> getAuthoritiesFromToken(Claims claims) {
        return Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
