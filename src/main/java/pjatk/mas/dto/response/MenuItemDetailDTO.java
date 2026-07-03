package pjatk.mas.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public record MenuItemDetailDTO(
        Long id,
        String name,
        Integer preparationTime,
        BigDecimal price,
        List<IngredientQuantityDTO> ingredients,
        Set<Long> usedIngredientIds
) {}