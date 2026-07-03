package pjatk.mas.repositories;

import pjatk.mas.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByPhoneNumber(String phoneNumber);
    Optional<Client> findByEmailAddress(String emailAddress);
}
