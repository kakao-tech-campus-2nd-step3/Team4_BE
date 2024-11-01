package linkfit.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;
import javax.crypto.SecretKey;
import linkfit.dto.Token;
import linkfit.exception.InvalidTokenException;
import linkfit.status.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long expirationTime;
    private final Long masterId;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final int BEARER_PREFIX_LENGTH = 7;
    private static final String ID = "id";
    private static final String ROLE = "role";

    public JwtUtil(@Value("${jwt.expiration-time}") long expirationTime,
        @Value("${jwt.master.id}") Long masterId) {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.expirationTime = expirationTime;
        this.masterId = masterId;
    }

    public String generateToken(Role role, Long id, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
            .claim(ID, id)
            .claim(ROLE, role)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public Token parseToken(String token) {
        if (isMasterToken(token)) {
            Role role = getTokenRole(token);
            return new Token(role, masterId);
        }
        try {
            Claims claims = extractAllClaims(token);
            if (isTokenExpired(claims.getExpiration())) {
                throw new InvalidTokenException("token.expired");
            }
            Role role = Role.valueOf((String) claims.get(ROLE));
            Long id = Long.valueOf(claims.get(ID).toString());
            return new Token(role, id);
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
            .parseClaimsJws(token)
            .getBody();
    }

    private boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

    private boolean isMasterToken(String token) {
        return token.startsWith("mastertoken-");
    }

    private Role getTokenRole(String token) {
        String role = token.substring(12);
        if (role.equals("user")) {
            return Role.USER;
        } else {
            return Role.TRAINER;
        }

    }
}
