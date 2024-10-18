package linkfit.entity;

import static linkfit.status.GymStatus.WAITING;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import linkfit.dto.GymDetailResponse;
import linkfit.dto.GymRegisterWaitingResponse;
import linkfit.status.GymStatus;

@Entity
@Table(name = "GYM_TB")
public class Gym {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    private String description;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private GymStatus status = WAITING;

    protected Gym() {
    }

    public Gym(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public GymRegisterWaitingResponse toDTO() {
        return new GymRegisterWaitingResponse(id, name, location);
    }

    public GymDetailResponse toDetailDTO(List<GymImage> images) {
        return new GymDetailResponse(id, name, location, description,
            images.stream().map(GymImage::getImageUrl).toList());
    }

    public void approval() {
        status = GymStatus.APPROVAL;
    }

    public void refuse() {
        status = GymStatus.REFUSE;
    }

    public void updateGym(String description) {
        this.description = description;
    }
}