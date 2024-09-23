package linkfit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import linkfit.dto.SportsResponse;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLDelete(sql = "UPDATE sports SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class Sports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String name;

    // 논리 삭제를 위한 필드
    private boolean deleted = false;

    public Sports() {}

    public Sports(String name) {
        this.name = name;
    }

    public Sports(Long id, String name) {
        this.id = id;
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
