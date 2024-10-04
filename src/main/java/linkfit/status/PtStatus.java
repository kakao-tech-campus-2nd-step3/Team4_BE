package linkfit.status;

public enum PtStatus {
    WAITING,    // 대기중인 제안
    RECALL,     // 트레이너가 회수한 제안
    REFUSE,     // 일반 회원이 거절한 제안
    APPROVAL,   // 성사된 제안
    COMPLETE    // 완료된 PT
}
