package linkfit.dto;

import linkfit.entity.Trainer;

public class TrainerProfileResponse {

    public TrainerProfileResponse() {
    }

    public TrainerProfileResponse(Trainer trainer) {
        this.name = trainer.getName();
        this.gender = trainer.getGender();
        this.profileImageUrl = trainer.getProfileImageUrl();
        this.gymName = trainer.getGymName();
    }

    private String name;

    private String gender;

    private String profileImageUrl;

    private String gymName;

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getGymName() {
        return gymName;
    }


}