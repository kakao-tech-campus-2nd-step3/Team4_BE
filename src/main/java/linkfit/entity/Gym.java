
package linkfit.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "GYM_TB")
public class Gym {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String gymLocation;

    @Column(nullable = false)
    private String gymName;

    protected Gym() {}

    public String getGymLocation() {
        return gymLocation;
    }

    public String getGymName() {
        return gymName;
    }
}