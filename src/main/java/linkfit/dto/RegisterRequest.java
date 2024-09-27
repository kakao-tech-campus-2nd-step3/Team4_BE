package linkfit.dto;

import linkfit.entity.Person;
import linkfit.exception.PasswordMismatchException;

public abstract class RegisterRequest<T extends Person<?>> {

	private final String email;
	private final String password;
	private final String passwordConfirm;
	private final String name;

	public RegisterRequest(String email, String password, String passwordConfirm, String name) {
		this.email = email;
		this.password = password;
		this.passwordConfirm = passwordConfirm;
		this.name = name;
	}

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

	public abstract T toEntity();
}
