package linkfit.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import linkfit.exception.InvalidTokenException;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.util.Date;

@Component
public class JwtUtil {

	private final SecretKey secretKey;
    private final long expirationMs;

    public JwtUtil() {
    	this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); 
        this.expirationMs = 1800000;
    }

    public String generateToken(Long id, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

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
            throw new InvalidTokenException("Invalid or expired token");
        }
    }
}
