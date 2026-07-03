package pjatk.mas.repositories;

import pjatk.mas.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByUsername(String username);
    boolean existsByUsername(String username);
}
