package linkfit.dto;

import linkfit.entity.Trainer;

public class TrainerRegisterRequest extends RegisterRequest<Trainer> {

    private String gender;

    public String getGender() {
        return gender;
    }

    public Trainer toEntity() {
        return new Trainer(getEmail(), getPassword(), getName(), gender);
    }
}