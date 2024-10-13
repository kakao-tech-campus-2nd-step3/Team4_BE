package linkfit.dto;

public record ReceivePtSuggestResponse(Long ptId, Long trainerId, String trainerName,
                                       String trainerProfileImageUrl, String gymName,
                                       int totalCount, int price) {

}
