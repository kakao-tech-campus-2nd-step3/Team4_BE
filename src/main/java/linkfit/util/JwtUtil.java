package linkfit.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import java.util.Date;

import javax.crypto.SecretKey;

import linkfit.exception.InvalidTokenException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long expirationTime;
    private final String masterToken;
    private final Long masterId;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final int BEARER_PREFIX_LENGTH = 7;
    private static final String ID = "id";

    public JwtUtil(@Value("${jwt.expiration-time}") long expirationTime,
        @Value("${jwt.master-token}") String masterToken,
        @Value("${jwt.master-id}") Long masterId) {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.expirationTime = expirationTime;
        this.masterToken = masterToken;
        this.masterId = masterId;
    }

    public String generateToken(Long id, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
            .setSubject(email)
            .claim(ID, id)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public Long parseToken(String token) {
        if (isMasterToken(token)) {
            return masterId;
        }
        try {
            Claims claims = extractAllClaims(token);
            if (isTokenExpired(claims.getExpiration())) {
                throw new InvalidTokenException("token.expired");
            }
            return claims.get(ID, Long.class);
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("token.expired");
        } catch (SignatureException e) {
            throw new InvalidTokenException("token.signature.invalid");
        } catch (Exception e) {
            throw new InvalidTokenException("invalid.token");
        }
    }

    private Claims extractAllClaims(String token) throws ExpiredJwtException, SignatureException {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .build()
            .parseSignedClaims(token)
            .getBody();
    }

    private boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

    private boolean isMasterToken(String token) {
        return masterToken.equals(token);
    }
}
