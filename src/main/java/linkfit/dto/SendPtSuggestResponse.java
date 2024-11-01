package linkfit.dto;

import linkfit.status.PtStatus;

public record SendPtSuggestResponse(Long id, String userName, String profileImageUrl,
                                    int totalCount, int price, PtStatus status) {

}