package linkfit.dto;

import linkfit.entity.User;
import linkfit.status.PtStatus;

public record PtSuggestionResponse(Long id, User user, int totalCount, int price, PtStatus status) {

}