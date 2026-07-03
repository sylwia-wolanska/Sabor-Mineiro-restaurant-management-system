package pjatk.mas.dto.response;

import java.math.BigDecimal;

public record IngredientDTO(
        Long id,
        String name,
        BigDecimal basePrice
) {}