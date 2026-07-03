package pjatk.mas.repositories;

import pjatk.mas.models.MenuItemQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemQuantityRepository extends JpaRepository<MenuItemQuantity, Long> {
}
