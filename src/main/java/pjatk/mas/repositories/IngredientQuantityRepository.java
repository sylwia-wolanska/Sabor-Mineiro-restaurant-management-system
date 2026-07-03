package pjatk.mas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pjatk.mas.models.IngredientQuantity;

public interface IngredientQuantityRepository extends JpaRepository<IngredientQuantity, Long> {

    @Query("SELECT COUNT(iq) > 0 FROM IngredientQuantity iq WHERE iq.menuItem.id = :menuItemId AND iq.ingredient.id = :ingredientId")
    boolean existsByMenuItemIdAndIngredientId(@Param("menuItemId") Long menuItemId, @Param("ingredientId") Long ingredientId);
}