package linkfit.dto;

import jakarta.validation.constraints.NotNull;
import linkfit.entity.Sports;

public class SportsRequest {

    @NotNull
    private String name;

    public SportsRequest() {}

    public SportsRequest(@NotNull String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sports toEntity() {
        return new Sports(this.name);
    }
}