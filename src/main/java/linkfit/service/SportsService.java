package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.DUPLICATE_NAME;

import linkfit.dto.SportsRequest;
import linkfit.entity.Sports;
import linkfit.exception.DuplicateException;
import linkfit.repository.SportsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SportsService {

    private SportsRepository sportsRepository;

    @Autowired
    public SportsService(SportsRepository sportsRepository) {
        this.sportsRepository = sportsRepository;
    }

    public void registerSport(SportsRequest sportsRequest) {
        if(sportsRepository.existsByName(sportsRequest.getName())) {
            throw new DuplicateException(DUPLICATE_NAME);
        }
        sportsRepository.save(sportsRequest.toEntity());
    }
}
