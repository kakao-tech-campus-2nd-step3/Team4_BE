package linkfit.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import linkfit.dto.LoginRequest;
import linkfit.dto.RegisterRequest;
import linkfit.entity.PersonEntity;

@Service
public class AuthService<T extends PersonEntity> {

    private final PersonService<T> personService;
    private final ImageUploadService imageUploadService;

    public AuthService(@Qualifier("personService") PersonService<T> personService, ImageUploadService imageUploadService) {
        this.personService = personService;
        this.imageUploadService = imageUploadService;
    }

    private final String secret = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public void register(RegisterRequest<T> request, MultipartFile profileImage) {
        T entity = request.toEntity();
        personService.existsByEmail(request.getEmail());
        request.verifyPassword();
        imageUploadService.profileImageSave(entity, profileImage);
        personService.save(entity);
    }

    public String login(LoginRequest request) {
        T entity = personService.findByEmail(request.getEmail());
        entity.validatePassword(request.getPassword());
        String token = grantAccessTokenUser(entity);
        return token;
    }

    private String grantAccessTokenUser(T entity) {
        return Jwts.builder()
                .setSubject(entity.getId().toString())
                .claim("userEmail", entity.getEmail())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }
}
