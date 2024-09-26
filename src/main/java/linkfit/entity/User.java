package linkfit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import linkfit.dto.UserProfileResponse;
import linkfit.dto.UserProfileRequest;

@Entity
@Table(name = "USER_TB", indexes = @Index(name = "idx_user_email", columnList = "email"))
public class User extends Person {

    @Column(nullable = false)
    private String local;

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    protected User() {
        super();
    }

    public User(String email, String password, String name, String local) {
        super(email, password, name);
        this.local = local;
    }

    public User Update(UserProfileRequest request) {
        User newUser = new User();
        newUser.setName(request.name());
        newUser.setLocal(request.local());
        return newUser;
    }

    public UserProfileResponse toDto() {
        return new UserProfileResponse(getName(), getLocal(), getProfileImageUrl());
    }
}
