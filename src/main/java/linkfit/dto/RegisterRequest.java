package linkfit.dto;

import org.springframework.web.multipart.MultipartFile;

import linkfit.entity.Person;
import linkfit.exception.PasswordMismatchException;

public class RegisterRequest<T extends Person> {

    private String email;
    private String password;
    private String passwordConfirm;
    private String name;
    private MultipartFile profileImage;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public String getName() {
        return name;
    }

    public MultipartFile getProfileImage() {
        return profileImage;
    }

    public void verifyPassword() {
        if (!this.password.equals(this.passwordConfirm)) {
            throw new PasswordMismatchException("Passwords do not match.");
        }
    }

    public T toEntity() {
        throw new UnsupportedOperationException("This method should be overridden in subclasses.");
    }
}