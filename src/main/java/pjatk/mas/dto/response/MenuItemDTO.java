package pjatk.mas.dto.response;

import java.math.BigDecimal;

public record MenuItemDTO(
        Long id,
        String name,
        Integer preparationTime,
        BigDecimal price,
        Integer ingredientCount
) {}