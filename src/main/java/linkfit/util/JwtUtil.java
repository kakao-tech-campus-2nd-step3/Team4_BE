package linkfit.util;

import static linkfit.exception.GlobalExceptionHandler.INVALID_TOKEN;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import linkfit.exception.InvalidTokenException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.util.Date;

@Component
public class JwtUtil {

	private final SecretKey secretKey;
	
	private final long expirationTime;

    public JwtUtil(@Value("${jwt.expiration-time}") long expirationTime) {
    	this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    	this.expirationTime = expirationTime;
    }

    public String generateToken(Long id, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(email)
                .claim("id", id)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
    
    public Long parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getEncoded()))
                    .build()
                    .parseSignedClaims(token.replace("Bearer ", ""))
                    .getPayload()
                    .get("id", Long.class);
        } catch(Exception e) {
            throw new InvalidTokenException(INVALID_TOKEN);
        }
    }
}
