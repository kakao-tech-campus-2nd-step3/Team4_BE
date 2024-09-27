package linkfit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import linkfit.dto.UserProfileResponse;
import linkfit.dto.UserProfileRequest;

@Entity
@Table(name = "USER_TB", indexes = @Index(name = "IDX_USER_EMAIL", columnList = "EMAIL"))
public class User extends Person<UserProfileResponse> {

    @Column(nullable = false)
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    protected User() {
        super();
    }

    public User(String email, String password, String name, String location) {
        super(email, password, name);
        this.location = location;
    }

    public void update(UserProfileRequest request) {
        this.setName(request.name());
        this.setLocation(request.location());
    }

    @Override
    public UserProfileResponse toDto() {
        return new UserProfileResponse(getName(), getLocation(), getProfileImageUrl());
    }
}