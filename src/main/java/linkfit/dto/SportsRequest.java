package linkfit.dto;

import jakarta.validation.constraints.NotNull;
import linkfit.entity.Sports;

public class SportsRequest {

    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public Sports toEntity() {
        return new Sports(this.name);
    }
}