package pjatk.mas.repositories;

import pjatk.mas.models.Reservation;
import pjatk.mas.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByClient(Client client);
}
