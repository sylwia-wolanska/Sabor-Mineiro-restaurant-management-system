package pjatk.mas.mappers;

import org.springframework.stereotype.Component;
import pjatk.mas.dto.response.*;
import pjatk.mas.models.*;

@Component
public class ReservationMapper {

    public ReservationSummaryDTO toSummaryDTO(Reservation reservation) {
        return new ReservationSummaryDTO(
                reservation.getId(),
                reservation.getClient().getName() + " " + reservation.getClient().getSurname(),
                reservation.getDateTime(),
                reservation.getCountOfPeople(),
                reservation.getDiningTable().getId(),
                reservation.getReservationStatus().name(),
                reservation.getClient().getId()
        );
    }

    public ReservationDetailDTO toDetailDTO(Reservation reservation) {
        boolean canReschedule =
                reservation.getReservationStatus() == ReservationStatus.ACCEPTED
                        || reservation.getReservationStatus() == ReservationStatus.RESCHEDULED;

        return new ReservationDetailDTO(
                reservation.getId(),
                reservation.getClient().getName() + " " + reservation.getClient().getSurname(),
                reservation.getClient().getId(),
                reservation.getDateTime(),
                reservation.getCountOfPeople(),
                reservation.getDiningTable().getId(),
                reservation.getDiningTable().getMaxPeople(),
                reservation.getDiningTable().isOccupied(),
                reservation.getReservationStatus().name(),
                canReschedule
        );
    }
}