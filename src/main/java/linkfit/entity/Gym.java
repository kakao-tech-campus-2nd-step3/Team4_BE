
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
    
    // 0: 승인x, 1: 승인o
    @Column(nullable = false, columnDefinition = "integer defalut 0")
    private int status;

    protected Gym() {
    }
    
    public Gym(String location, String name) {
    	this.location = location;
    	this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}