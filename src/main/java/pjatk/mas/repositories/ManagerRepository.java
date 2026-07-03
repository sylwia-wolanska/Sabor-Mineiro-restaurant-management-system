package pjatk.mas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.mas.models.Manager;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
}
