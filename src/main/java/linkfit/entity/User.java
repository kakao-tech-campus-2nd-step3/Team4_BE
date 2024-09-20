package linkfit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Users")
public class User extends PersonEntity{

	@Column(nullable = false)
    private String local;
	
	public String getLocal() {
		return local;
	}
	
	public User() {
		super();
	}
	
	public User(String email, String password, String name, String local) {
        super(email, password, name);
        this.local = local;
    }
}
