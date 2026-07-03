package pjatk.mas.dto.response;

import java.math.BigDecimal;

public record IngredientQuantityDTO(
        Long ingredientQuantityId,
        Long ingredientId,
        String ingredientName,
        BigDecimal basePrice,
        BigDecimal quantity,
        BigDecimal subtotal
) {}