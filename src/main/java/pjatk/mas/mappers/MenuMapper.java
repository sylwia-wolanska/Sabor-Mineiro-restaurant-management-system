package pjatk.mas.mappers;

import org.springframework.stereotype.Component;
import pjatk.mas.dto.response.*;
import pjatk.mas.models.*;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MenuMapper {

    public MenuItemDTO toMenuItemDTO(MenuItem item) {
        return new MenuItemDTO(
                item.getId(),
                item.getName(),
                item.getPreparationTime(),
                item.getPrice(),
                item.getIngredientQuantities().size()
        );
    }

    public MenuItemDetailDTO toMenuItemDetailDTO(MenuItem item) {
        var ingredients = item.getIngredientQuantities().stream()
                .map(iq -> new IngredientQuantityDTO(
                        iq.getId(),
                        iq.getIngredient().getId(),
                        iq.getIngredient().getName(),
                        iq.getIngredient().getBasePrice(),
                        iq.getQuantity(),
                        iq.getIngredient().getBasePrice().multiply(iq.getQuantity())
                ))
                .toList();

        Set<Long> usedIngredientIds = item.getIngredientQuantities().stream()
                .map(iq -> iq.getIngredient().getId())
                .collect(Collectors.toSet());

        return new MenuItemDetailDTO(
                item.getId(),
                item.getName(),
                item.getPreparationTime(),
                item.getPrice(),
                ingredients,
                usedIngredientIds
        );
    }

    public IngredientDTO toIngredientDTO(Ingredient ingredient) {
        return new IngredientDTO(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getBasePrice()
        );
    }

    public DiningTableDTO toDiningTableDTO(DiningTable table) {
        return new DiningTableDTO(
                table.getId(),
                table.getMaxPeople(),
                table.isOccupied()
        );
    }
}