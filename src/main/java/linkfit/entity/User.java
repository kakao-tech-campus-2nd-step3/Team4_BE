package linkfit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import linkfit.dto.UserProfileResponse;
import linkfit.dto.UserProfileRequest;

@Entity
@Table(name = "Users")
public class User extends PersonEntity{

	@Column(nullable = false)
    private String location;
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public User() {
		super();
	}
	
	public User(String email, String password, String name, String location) {
        super(email, password, name);
        this.location = location;
	}
	
	public User Update(UserProfileRequest request) {
		User newUser = new User();
		newUser.setName(request.getName());
		newUser.setLocation(request.getLocal());
		return newUser;
	}
	
	public UserProfileResponse toDto() {
		return new UserProfileResponse(getName(), getLocation(), getProfileImageUrl());
	}
}
