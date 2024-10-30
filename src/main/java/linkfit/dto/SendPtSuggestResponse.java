package linkfit.dto;

import linkfit.status.PtStatus;

public record SendPtSuggestResponse(Long id, String userName, String ProfileImageUrl,
                                    int totalCount, int price, PtStatus status) {

}