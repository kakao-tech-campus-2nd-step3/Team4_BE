package linkfit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import linkfit.dto.UserRequest;

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

    public User(User user, UserRequest userRequest) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.name = user.getName();
        this.local = userRequest.getLocal();
        // 프로필 이미지 멀티파트파일의 url 부분은 추후 구현예정으로 임시로 스트링변환 처리
        this.profileImageUrl = userRequest.getProfileImageUrl().toString();
        this.goal = userRequest.getGoal();
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
