package linkfit.dto;

import linkfit.entity.Sports;

public record SportsRequest(String name) {

    public SportsRequest() {
        this("");
    }

    public Sports toEntity() {
        return new Sports(name);
    }
}