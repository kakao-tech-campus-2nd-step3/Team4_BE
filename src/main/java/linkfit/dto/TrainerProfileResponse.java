package linkfit.dto;

import linkfit.status.TrainerGender;

public record TrainerProfileResponse(String name, TrainerGender gender, String profileImageUrl,
                                     String gymName) {

}