package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.DUPLICATE_NAME;
import static linkfit.exception.GlobalExceptionHandler.NOT_EXIST_ID;

import java.util.List;
import linkfit.dto.SportsRequest;
import linkfit.dto.SportsResponse;
import linkfit.entity.Sports;
import linkfit.exception.DuplicateException;
import linkfit.exception.InvalidIdException;
import linkfit.repository.SportsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SportsService {

    private final SportsRepository sportsRepository;

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

    public List<SportsResponse> getAllSports(Pageable pageable) {
        return sportsRepository.findAll(pageable)
            .stream()
            .map(Sports::toDto)
            .toList();
    }

    public Sports findSportsById(Long id) {
        return sportsRepository.findById(id)
            .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));
    }

    public void updateSports(Long id, SportsRequest sportsRequest) {
        Sports sports = sportsRepository.findById(id)
            .orElseThrow(()-> new InvalidIdException(NOT_EXIST_ID));
        isDuplicateName(sports, sportsRequest);
        sportsRepository.save(new Sports(id, sportsRequest.getName()));
    }

    private void isDuplicateName(Sports sports, SportsRequest sportsRequest) {
        if(sports.getName().equals(sportsRequest.getName())) {
            throw new DuplicateException(DUPLICATE_NAME);
        }
    }
}
