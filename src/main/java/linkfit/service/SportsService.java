package linkfit.service;

import java.util.List;

import linkfit.dto.SportsRequest;
import linkfit.dto.SportsResponse;
import linkfit.entity.Sports;
import linkfit.exception.DuplicateException;
import linkfit.exception.NotFoundException;
import linkfit.repository.SportsRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SportsService {

    private final SportsRepository sportsRepository;

    public SportsService(SportsRepository sportsRepository) {
        this.sportsRepository = sportsRepository;
    }

    public void registerSport(SportsRequest sportsRequest) {
        validateSportAlreadyExists(sportsRequest);
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
            .orElseThrow(() -> new NotFoundException("not.found.sports"));
    }

    public void renameSports(Long id, SportsRequest sportsRequest) {
        Sports sports = getSportsById(id);
        validateSportAlreadyExists(sportsRequest);
        sports.rename(sportsRequest);
        sportsRepository.save(sports);
    }

    public void deleteSports(Long id) {
        validateSportExists(id);
        sportsRepository.deleteById(id);
    }

    public Sports getSportsById(Long id) {
        return sportsRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("not.found.sports"));
    }

    private void validateSportAlreadyExists(SportsRequest sportsRequest) {
        if (sportsRepository.existsByName(sportsRequest.name())) {
            throw new DuplicateException("already.exist.sports");
        }
    }

    private void validateSportExists(Long id) {
        if (!sportsRepository.existsById(id)) {
            throw new NotFoundException("not.found.sports");
        }
    }
}