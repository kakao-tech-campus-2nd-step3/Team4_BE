package linkfit.dto;

import org.springframework.web.multipart.MultipartFile;

import linkfit.entity.User;
import linkfit.exception.PasswordMismatchException;

public class UserRegisterRequest {

	private String email;
	private String password;
	private String passwordConfirm;
	private String name;
	private String local;
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
	
	public String getLocal() {
		return local;
	}
	
	public String getName() {
		return name;
	}
	
	public User toEntity() {
		User user = new User(email, password, name, local);
        return user;
    }
	
	public void verifyPassword() {
		if(!this.password.equals(this.passwordConfirm)) {
			throw new PasswordMismatchException("Password does not match.");
		}
	}
}
