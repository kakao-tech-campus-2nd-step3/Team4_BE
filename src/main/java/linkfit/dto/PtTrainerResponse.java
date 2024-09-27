package linkfit.dto;

import linkfit.entity.Trainer;

public record PtTrainerResponse(Long trainerId, String trainerName, String trainerProfileImageUrl, String gymName) {
    public PtTrainerResponse(Trainer trainer) {
        this(trainer.getId(), trainer.getName(), trainer.getProfileImageUrl(), trainer.getGym().getGymName());
    }
}
