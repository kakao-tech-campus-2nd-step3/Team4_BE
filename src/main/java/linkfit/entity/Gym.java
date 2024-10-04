
package linkfit.entity;

import jakarta.persistence.*;
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
    
    // 0: 승인x, 1: 승인o
    @Column(nullable = false, columnDefinition = "integer defalut 0")
    private int status;

    @Column(nullable = false)
    private String location;

    private String description = null;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private GymStatus status = GymStatus.WAITING;

    protected Gym() {
    }
    
    public Gym(String location, String name) {
    	this.location = location;
    	this.name = name;
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