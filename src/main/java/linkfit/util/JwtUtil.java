package linkfit.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import java.util.Date;
import java.util.Locale;
import javax.crypto.SecretKey;

import linkfit.exception.InvalidTokenException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long expirationTime;
    private final String masterToken;
    private final Long masterId;
    private final MessageSource messageSource;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String ID = "id";

    public JwtUtil(@Value("${jwt.expiration-time}") long expirationTime,
        @Value("${jwt.master-token}") String masterToken,
        @Value("${jwt.master-id}") Long masterId,
        MessageSource messageSource) {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.expirationTime = expirationTime;
        this.masterToken = masterToken;
        this.masterId = masterId;
        this.messageSource = messageSource;
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
        String processedToken = token.replace(BEARER_PREFIX, "");
        if (isMasterToken(processedToken)) {
            return masterId;
        }
        try {
            isValidToken(processedToken);
            return extractAllClaims(token).get(ID, Long.class);
        } catch (Exception e) {
            throw new InvalidTokenException(
                messageSource.getMessage("invalid.token", null, Locale.getDefault()));
        }

    }

    private Claims extractAllClaims(String token) throws ExpiredJwtException, SignatureException {
        return Jwts.parser()
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.getEncoded()))
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public boolean isValidToken(String token) {
        String processedToken = token.replace(BEARER_PREFIX, "");
        if (isMasterToken(processedToken)) {
            return true;
        }
        try {
            Claims claims = extractAllClaims(processedToken);
            return !isTokenExpired(processedToken);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    private boolean isMasterToken(String token) {
        return masterToken.equals(token);
    }
}
