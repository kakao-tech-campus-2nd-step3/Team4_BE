package linkfit.entity;

import static linkfit.exception.GlobalExceptionHandler.NOT_MATCH_PASSWORD;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import linkfit.exception.PasswordMismatchException;

@MappedSuperclass
public abstract class Person<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String profileImageUrl;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    protected Person() {
    }

    public Person(String email, String passowrd, String name) {
        this.email = email;
        this.password = passowrd;
        this.name = name;
    }

    public void validatePassword(String inputPassword) {
        if (!inputPassword.equals(this.password)) {
            throw new PasswordMismatchException(NOT_MATCH_PASSWORD);
        }
    }

    public abstract T toDto();
}
