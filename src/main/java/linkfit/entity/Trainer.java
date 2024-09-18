package linkfit.entity;

import jakarta.persistence.*;

@Entity
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "GYM_ID", nullable = false)
    private Gym gym;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String gender;

    private String profileImageUrl;

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getGymName() {
        return gym.getGymName();
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public Trainer() {
    }
}