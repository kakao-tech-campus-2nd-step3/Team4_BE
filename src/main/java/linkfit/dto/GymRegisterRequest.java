package linkfit.dto;

import linkfit.entity.Gym;

public class GymRegisterRequest {

    private String name;

    private String location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Gym toEntity() {
        return new Gym(name, location);
    }
}
