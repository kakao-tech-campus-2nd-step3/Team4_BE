package linkfit.dto;

import linkfit.entity.Trainer;
import linkfit.status.TrainerGender;

public record TrainerRegisterRequest(String email, String password, String name,
                                     TrainerGender gender) {

    public Trainer toEntity() {
        return new Trainer(email, password(), name(), gender);
    }
}
