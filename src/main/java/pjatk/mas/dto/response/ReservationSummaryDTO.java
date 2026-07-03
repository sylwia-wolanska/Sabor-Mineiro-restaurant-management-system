package pjatk.mas.dto.response;

import java.time.LocalDateTime;

public record ReservationSummaryDTO(
        Long id,
        String clientName,
        LocalDateTime dateTime,
        Integer countOfPeople,
        Long tableId,
        String reservationStatus,
        Long clientId
) {}