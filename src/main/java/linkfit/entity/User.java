package linkfit.entity;

import jakarta.persistence.*;

import linkfit.dto.UserProfileRequest;
import linkfit.dto.UserProfileResponse;
import linkfit.exception.PasswordMismatchException;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Table(name = "USER_TB", indexes = @Index(name = "IDX_USER_EMAIL", columnList = "EMAIL"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String profileImageUrl;

    @Column(nullable = false)
    private String location;

    @Value("${defaultImageUrl}")
    private String defaultImageUrl;

    @Value("${defaultImageUrl}")
    private String defaultProfileImageUrl;

    protected User() {
    }

    public User(String email, String password, String name, String location) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.location = location;
    }

    @PrePersist
    public void prePersist() {
        if (this.profileImageUrl == null || this.profileImageUrl.isEmpty()) {
            this.profileImageUrl = defaultProfileImageUrl;
        }
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void update(UserProfileRequest request) {
        this.setName(request.name());
        this.setLocation(request.location());
    }

    public void validatePassword(String inputPassword) {
        if (!inputPassword.equals(this.password)) {
            throw new PasswordMismatchException("not.match.password");
        }
    }

    public UserProfileResponse toDto() {
        return new UserProfileResponse(getName(), getLocation(), getProfileImageUrl());
    }
}