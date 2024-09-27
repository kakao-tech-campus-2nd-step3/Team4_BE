package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.DUPLICATE_EMAIL;

import linkfit.exception.DuplicateException;
import org.springframework.stereotype.Service;

import linkfit.entity.Person;
import linkfit.repository.PersonRepository;

@Service
public class PersonService<T extends Person<?>> {

    private final PersonRepository<T> personRepository;

    public PersonService(PersonRepository<T> personRepository) {
        this.personRepository = personRepository;
    }

    public void existsByEmail(String email) {
        if (personRepository.existsByEmail(email)) {
            throw new DuplicateException(DUPLICATE_EMAIL);
        }
    }

    public void save(T entity) {
        personRepository.save(entity);
    }

    public T findByEmail(String email) {
        return personRepository.findByEmail(email)
            .orElseThrow(() -> new DuplicateException(DUPLICATE_EMAIL));
    }
}
