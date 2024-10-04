
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

    private String description = null;

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

    public String getDescription() {
        return description;
    }

    public GymStatus getStatus() {
        return status;
    }

    public GymRegisterWaitingResponse toDTO() {
        return new GymRegisterWaitingResponse(id, name, location);
    }

    public void approval() {
        status = GymStatus.APPROVAL;
    }

    public void refuse() {
        status = GymStatus.REFUSE;
    }
}