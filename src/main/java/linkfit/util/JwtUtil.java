package linkfit.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
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

    public String generateToken(String userId, String userEmail) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(userId)
                .claim("userEmail", userEmail)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
