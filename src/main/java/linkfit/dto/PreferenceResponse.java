package linkfit.dto;

public class PreferenceResponse {
	
	private Long userId;
	private String name;
	private String inbodyImageUrl;
	private String goal;
	private String profileImageUrl;
	
	public PreferenceResponse(Long userId, String name, String inbodyImageUrl, String goal,
			String profileImageUrl) {
		this.userId = userId;
		this.name = name;
		this.inbodyImageUrl = inbodyImageUrl;
		this.goal = goal;
		this.profileImageUrl = profileImageUrl;
	}
}
