package linkfit.dto;

import linkfit.entity.Trainer;

public record TrainerRegisterRequest(String email, String password, String name, String gender) {

    public Trainer toEntity() {
        return new Trainer(email, password(), name(), gender);
    }
}
