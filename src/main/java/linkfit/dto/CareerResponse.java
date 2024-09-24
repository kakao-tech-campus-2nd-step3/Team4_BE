package linkfit.dto;

import linkfit.entity.Career;

public class CareerResponse {

    private Long id;
    private Long trainerId;
    private String career;

    public CareerResponse(Career career) {
        this.id = career.getId();
        this.trainerId = career.getTrainer().getId();
        this.career = career.getCareer();
    }

    public Long getId() {
        return id;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public String getCareer() {
        return career;
    }
}