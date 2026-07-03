package pjatk.mas.repositories;

import pjatk.mas.models.DeliveryDriver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryDriverRepository extends JpaRepository<DeliveryDriver, Long> {
}
