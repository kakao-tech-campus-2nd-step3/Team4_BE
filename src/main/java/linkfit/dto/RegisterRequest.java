package linkfit.dto;

import linkfit.entity.Person;

public abstract class RegisterRequest<T extends Person<?>> {

    private final String email;
    private final String password;
    private final String name;

    protected RegisterRequest() {
        this.email = "";
        this.password = "";
        this.name = "";
    }

    public RegisterRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
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

    public abstract T toEntity();
}
