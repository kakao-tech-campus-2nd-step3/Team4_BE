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
        isExistSports(sportsRequest);
        sportsRepository.save(sportsRequest.toEntity());
    }

    private void isExistSports(SportsRequest sportsRequest) {
        if (sportsRepository.existsByName(sportsRequest.name())) {
            throw new DuplicateException("duplicate.name");
        }
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

    public void updateSports(Long id, SportsRequest sportsRequest) {
        Sports sports = sportsRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("not.found.sports"));
        isDuplicateName(sports, sportsRequest);
        sportsRepository.save(new Sports(id, sportsRequest.name()));
    }

    private void isDuplicateName(Sports sports, SportsRequest sportsRequest) {
        if (sports.getName().equals(sportsRequest.name())) {
            throw new DuplicateException("duplicate.name");
        }
    }

    public void deleteSports(Long id) {
        isExist(id);
        sportsRepository.deleteById(id);
    }

    public Sports getSportsById(Long id) {
        return sportsRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("not.found.sports"));
    }

    private void isExist(Long id) {
        if (!sportsRepository.existsById(id)) {
            throw new NotFoundException("not.found.sports");
        }
    }
}
