package linkfit.service;

import java.util.List;
import linkfit.dto.PtResponse;
import linkfit.entity.OnGoingPt;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import linkfit.repository.OnGoingPtRepository;
import linkfit.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PtService {

    private final OnGoingPtRepository onGoingPtRepository;
    private final PersonRepository personRepository;

    @Autowired
    public PtService(
        OnGoingPtRepository onGoingPtRepository,
        PersonRepository personRepository
    ) {
        this.onGoingPtRepository = onGoingPtRepository;
        this.personRepository = personRepository;
    }

    public List<PtResponse> getAllPts(
        String authorization,
        Pageable pageable) {
        // 토큰 파싱하여 트레이너 정보 받아오기
        Trainer trainer = new Trainer();
        return onGoingPtRepository.findAllByTrainer(trainer, pageable)
            .stream()
            .map(OnGoingPt::getUser)
            .map(PtResponse::new)
            .toList();
    }
}
