package pjatk.mas.repositories;

import pjatk.mas.models.Order;
import pjatk.mas.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByClient(Client client);
}
