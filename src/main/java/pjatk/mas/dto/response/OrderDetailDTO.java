package pjatk.mas.dto.response;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

public record OrderDetailDTO(
        Long id,
        String clientName,
        String orderType,
        String orderStatus,
        BigDecimal totalPrice,
        Integer estimatedPrepTime,
        List<MenuItemQuantityDTO> items,

        // Pickup fields
        LocalTime hourOfPickup,

        // Delivery fields
        String fullAddress,
        String deliveryDriverName,

        // Waiter
        String waiterName,
        Long waiterId
) {}