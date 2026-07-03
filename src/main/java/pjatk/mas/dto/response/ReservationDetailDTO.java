package pjatk.mas.dto.response;

import java.time.LocalDateTime;

public record ReservationDetailDTO(
        Long id,
        String clientName,
        Long clientId,
        LocalDateTime dateTime,
        Integer countOfPeople,
        Long tableId,
        Integer tableMaxPeople,
        boolean tableOccupied,
        String reservationStatus,
        boolean canReschedule
) {}