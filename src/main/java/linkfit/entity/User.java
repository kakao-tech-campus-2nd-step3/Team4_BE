package linkfit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String local;

    private String profileImageUrl;

    private String goal;

    public User() {}

    public User(String email, String password, String name, String local, String profileImageUrl, String goal) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.local = local;
        this.profileImageUrl = profileImageUrl;
        this.goal = goal;
    }

    public User(Long id, String email, String password, String name, String local, String profileImageUrl, String goal) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.local = local;
        this.profileImageUrl = profileImageUrl;
        this.goal = goal;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getLocal() {
        return local;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getGoal() {
        return goal;
    }
}
