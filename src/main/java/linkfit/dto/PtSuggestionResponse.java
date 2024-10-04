package linkfit.dto;

import linkfit.entity.User;
import linkfit.status.PtStatus;

public record PtSuggestionResponse(Long id, String userName, String userProfileImageUrl,
                                   int totalCount, int price, PtStatus status) {

}