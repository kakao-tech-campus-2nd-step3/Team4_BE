package linkfit.dto;

import linkfit.entity.User;

public record PtSuggestionResponse(Long id, User user, int totalCount, int price, int status) {

}
