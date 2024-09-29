package linkfit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import linkfit.dto.SportsResponse;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "SPORTS_TB")
@SQLDelete(sql = "UPDATE sports SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class Sports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // 논리 삭제를 위한 필드
    private boolean deleted = Boolean.FALSE;

    protected Sports() {
    }

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
