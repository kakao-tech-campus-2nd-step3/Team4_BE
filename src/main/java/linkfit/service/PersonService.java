package linkfit.service;

import org.springframework.stereotype.Service;

import linkfit.entity.PersonEntity;
import linkfit.exception.ExistsEmailException;
import linkfit.exception.NotFoundEmailException;
import linkfit.repository.PersonRepository;

@Service
public class PersonService<T extends PersonEntity> {

    private final PersonRepository<T> personRepository;

    public PersonService(PersonRepository<T> personRepository) {
        this.personRepository = personRepository;
    }

    public void existsByEmail(String email) {
        if (personRepository.existsByEmail(email)) {
            throw new ExistsEmailException("This email already exists.");
        }
    }

    public void save(T entity) {
        personRepository.save(entity);
    }

    public T findByEmail(String email) {
        return personRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundEmailException("This email not found."));
    }
}
