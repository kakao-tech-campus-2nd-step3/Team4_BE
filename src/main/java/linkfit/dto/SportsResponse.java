package linkfit.dto;

public class SportsResponse {

    private Long id;
    private String name;

    public SportsResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
