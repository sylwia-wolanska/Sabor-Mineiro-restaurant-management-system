package pjatk.mas.dto.response;

import java.math.BigDecimal;

public record MenuItemQuantityDTO(
        Long menuItemId,
        String menuItemName,
        Integer preparationTime,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal subtotal
) {}