package linkfit.dto;

import linkfit.entity.PersonEntity;
import linkfit.exception.PasswordMismatchException;

public class RegisterRequest<T extends PersonEntity> {

	private String email;
	private String password;
	private String passwordConfirm;
	private String name;

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

	public void verifyPassword() {
		if (!this.password.equals(this.passwordConfirm)) {
			throw new PasswordMismatchException("Passwords do not match.");
		}
	}

	public T toEntity() {
		throw new UnsupportedOperationException("This method should be overridden in subclasses.");
	}
}
