package pjatk.mas.repositories;

import pjatk.mas.models.Waiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaiterRepository extends JpaRepository<Waiter, Long> {
}
