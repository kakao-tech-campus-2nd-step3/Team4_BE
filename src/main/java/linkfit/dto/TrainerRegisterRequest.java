package linkfit.dto;

import linkfit.entity.Trainer;

public class TrainerRegisterRequest extends RegisterRequest<Trainer> {

    private final String gender;

    public TrainerRegisterRequest(String email, String password, String passwordConfirm, String name, String gender) {
        super(email, password, passwordConfirm, name);
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    @Override
    public Trainer toEntity() {
        return new Trainer(getEmail(), getPassword(), getName(), gender);
    }
}
