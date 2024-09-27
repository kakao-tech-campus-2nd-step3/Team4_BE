
package linkfit.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "GYM_TB")
public class Gym {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String name;

    protected Gym() {}

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}