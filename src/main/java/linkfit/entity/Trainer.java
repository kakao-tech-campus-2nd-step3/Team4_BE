package linkfit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Trainers")
public class Trainer {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
