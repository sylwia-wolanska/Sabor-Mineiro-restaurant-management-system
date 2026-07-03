package pjatk.mas;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pjatk.mas.models.Order;
import pjatk.mas.models.OrderStatus;
import pjatk.mas.models.Reservation;
import pjatk.mas.models.ReservationStatus;
import pjatk.mas.repositories.OrderRepository;
import pjatk.mas.repositories.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReservationScheduler {

    private final ReservationRepository reservationRepository;
    private final OrderRepository orderRepository;

    public ReservationScheduler(ReservationRepository reservationRepository, OrderRepository orderRepository) {
        this.reservationRepository = reservationRepository;
        this.orderRepository = orderRepository;
    }

    @Scheduled(fixedRate = 300_000)
    @Transactional
    public void updateReservationStatuses() {
        LocalDateTime now = LocalDateTime.now();

        for (Reservation r : reservationRepository.findAll()) {

            if ((r.getReservationStatus() == ReservationStatus.ACCEPTED
                    || r.getReservationStatus() == ReservationStatus.RESCHEDULED)
                    && !r.getDateTime().isAfter(now)) {

                r.setReservationStatus(ReservationStatus.ONGOING);
                reservationRepository.save(r);

            } else if (r.getReservationStatus() == ReservationStatus.ONGOING && r.getDateTime().plusHours(2).isBefore(now)) {
                r.setReservationStatus(ReservationStatus.COMPLETED);
                reservationRepository.save(r);
            }
        }
    }

    @Scheduled(fixedRate = 86_400_000)
    @Transactional
    public void deleteCancelledRecords() {
        LocalDateTime cutoff = LocalDateTime.now().minusMonths(3);

        List<Reservation> oldReservations = reservationRepository.findAll().stream()
                .filter(r -> r.getReservationStatus() == ReservationStatus.CANCELLED && r.getDateTime().isBefore(cutoff))
                .toList();
        reservationRepository.deleteAll(oldReservations);

        List<Order> oldOrders = orderRepository.findAll().stream()
                .filter(o -> o.getOrderStatus() == OrderStatus.CANCELLED && o.getCancelledAt() != null && o.getCancelledAt().isBefore(cutoff))
                .toList();
        orderRepository.deleteAll(oldOrders);

        if (!oldReservations.isEmpty() || !oldOrders.isEmpty()) {
            System.out.println("[Scheduler] Deleted " + oldReservations.size() + " old cancelled reservations and "
                    + oldOrders.size() + " old cancelled orders.");
        }
    }
}