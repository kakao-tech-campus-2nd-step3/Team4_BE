package linkfit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import linkfit.dto.SportsResponse;

@Entity
public class Sports {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique=true)
    private String name;

    public Sports() {}

    public Sports(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public SportsResponse toDto() {
        return new SportsResponse(id, name);
    }
}
