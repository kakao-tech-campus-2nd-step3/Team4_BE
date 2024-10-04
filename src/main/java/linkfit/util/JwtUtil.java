package linkfit.util;

import static linkfit.exception.GlobalExceptionHandler.INVALID_TOKEN;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
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
            .claim("id", id)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public Long parseToken(String token) {
        String processedToken = token.replace("Bearer ", "");
        if (isMasterToken(processedToken)) {
            return masterId;
        }
        try {
            return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getEncoded()))
                .build()
                .parseSignedClaims(processedToken)
                .getPayload()
                .get("id", Long.class);
        } catch (Exception e) {
            throw new InvalidTokenException(INVALID_TOKEN);
        }
    }

    private boolean isMasterToken(String token) {
        return masterToken.equals(token);
    }
}
