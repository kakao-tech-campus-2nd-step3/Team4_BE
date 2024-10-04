package linkfit.dto;

import linkfit.status.PtStatus;

public record SendPtSuggestResponse(Long id, String userName, String userProfileImageUrl,
                                    int totalCount, int price, PtStatus status) {

}