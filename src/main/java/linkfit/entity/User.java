package linkfit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import linkfit.component.DefaultImageProvider;
import linkfit.dto.UserProfileRequest;
import linkfit.dto.UserProfileResponse;

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

    @Column(nullable = true)
    private String profileImageUrl;

    @Column(nullable = false)
    private String location;

    @Transient
    private static DefaultImageProvider defaultImageProvider;

    public static void setDefaultImageProvider(DefaultImageProvider provider) {
        defaultImageProvider = provider;
    }

    protected User() {
    }

    public User(String email, String password, String name, String location) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.location = location;
    }

    @PrePersist
    private void setDefaultProfileImageUrl() {
        if (this.profileImageUrl == null || this.profileImageUrl.isEmpty()) {
            this.profileImageUrl = defaultImageProvider.getDefaultImageUrl();
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

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void updateInfo(UserProfileRequest request) {
        name = request.name();
        location = request.location();
    }

    public UserProfileResponse toDto() {
        return new UserProfileResponse(getName(), getLocation(), getProfileImageUrl());
    }
}