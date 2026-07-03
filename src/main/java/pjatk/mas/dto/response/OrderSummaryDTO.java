package pjatk.mas.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record OrderSummaryDTO(
        Long id,
        String clientName,
        String orderType,
        String orderStatus,
        BigDecimal totalPrice,
        Integer estimatedPrepTime
) {}