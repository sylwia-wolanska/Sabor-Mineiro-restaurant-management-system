package pjatk.mas.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pjatk.mas.dto.response.ReservationDetailDTO;
import pjatk.mas.dto.response.ReservationSummaryDTO;
import pjatk.mas.mappers.ReservationMapper;
import pjatk.mas.models.*;
import pjatk.mas.repositories.*;
import pjatk.mas.session.ReservationWizardSession;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final DiningTableRepository diningTableRepository;
    private final ClientRepository clientRepository;
    private final ReservationMapper reservationMapper;

    public ReservationService(ReservationRepository reservationRepository, DiningTableRepository diningTableRepository,
                              ClientRepository clientRepository, ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.diningTableRepository = diningTableRepository;
        this.clientRepository = clientRepository;
        this.reservationMapper = reservationMapper;
    }

    @Transactional(readOnly = true)
    public List<ReservationSummaryDTO> getReservationsForPerson(Person person) {
        if (person instanceof Client client) {
            Client managed = clientRepository.findById(client.getId()).orElseThrow(() -> new RuntimeException("Client not found"));
            return reservationRepository.findByClient(managed).stream().map(reservationMapper::toSummaryDTO).toList();
        }
        return reservationRepository.findAll().stream().map(reservationMapper::toSummaryDTO).toList();
    }

    @Transactional(readOnly = true)
    public ReservationDetailDTO getReservationDetailById(Long id, Person person) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Reservation not found: " + id));

        if (person instanceof Client client && !reservation.getClient().getId().equals(client.getId())) {
            throw new IllegalStateException("Access denied");
        }

        return reservationMapper.toDetailDTO(reservation);
    }

    @Transactional(readOnly = true)
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Reservation not found: " + id));
    }

    @Transactional(readOnly = true)
    public DiningTable checkFreeTables(LocalDateTime dateTime, int numberOfPeople) {
        LocalDateTime windowStart = dateTime.minusHours(2);
        LocalDateTime windowEnd = dateTime.plusHours(2);

        return diningTableRepository.findAll().stream()
                .filter(table -> table.getMaxPeople() >= numberOfPeople)
                .filter(table -> table.getReservations().stream()
                        .filter(r -> r.getReservationStatus() != ReservationStatus.CANCELLED
                                && r.getReservationStatus() != ReservationStatus.COMPLETED)
                        .noneMatch(r -> {
                            LocalDateTime resTime = r.getDateTime();
                            return !resTime.isBefore(windowStart)
                                    && !resTime.isAfter(windowEnd);
                        }))
                .min((t1, t2) -> Integer.compare(t1.getMaxPeople(), t2.getMaxPeople()))
                .orElse(null);
    }

    public Reservation createReservation(ReservationWizardSession wizardSession, Client sessionClient) {
        Client client = clientRepository.findById(sessionClient.getId()).orElseThrow(() -> new IllegalArgumentException("Client not found"));

        if (wizardSession.getDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Reservation date must be in the future");
        }

        DiningTable table = diningTableRepository.findById(wizardSession.getFoundTableId()).orElseThrow(() -> new IllegalArgumentException("Table not found"));

        Reservation reservation = new Reservation(wizardSession.getDateTime(), wizardSession.getNumberOfPeople(), client, table);

        client.addReservation(reservation);
        return reservationRepository.save(reservation);
    }

    public ReservationDetailDTO updateReservationStatus(Long id, ReservationStatus newStatus, Person person) {
        Reservation reservation = getReservationById(id);
        ReservationStatus current = reservation.getReservationStatus();

        boolean valid = switch (current) {
            case ACCEPTED -> newStatus == ReservationStatus.ONGOING
                    || newStatus == ReservationStatus.CANCELLED
                    || newStatus == ReservationStatus.RESCHEDULED;
            case ONGOING -> newStatus == ReservationStatus.COMPLETED
                    || newStatus == ReservationStatus.CANCELLED;
            case RESCHEDULED -> newStatus == ReservationStatus.ACCEPTED
                    || newStatus == ReservationStatus.CANCELLED;
            case COMPLETED, CANCELLED -> false;
        };

        if (!valid) {
            throw new IllegalStateException("Invalid status transition: " + current + " → " + newStatus);
        }

        reservation.setReservationStatus(newStatus);
        return reservationMapper.toDetailDTO(reservationRepository.save(reservation));
    }

    public ReservationDetailDTO reschedule(Long reservationId, LocalDateTime newDateTime, Client sessionClient) {
        Reservation reservation = getReservationById(reservationId);

        if (!reservation.getClient().getId().equals(sessionClient.getId())) {
            throw new IllegalArgumentException("You can only reschedule your own reservations");
        }

        if (reservation.getReservationStatus() != ReservationStatus.ACCEPTED && reservation.getReservationStatus() != ReservationStatus.RESCHEDULED) {
            throw new IllegalStateException("Only ACCEPTED or RESCHEDULED reservations can be rescheduled");
        }

        if (newDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Reservation date must be in the future");
        }

        DiningTable newTable = checkFreeTables(
                newDateTime, reservation.getCountOfPeople());
        if (newTable == null) {
            throw new IllegalStateException("No tables available for the requested date and time");
        }

        reservation.setDateTime(newDateTime);
        reservation.setDiningTable(newTable);
        reservation.setReservationStatus(ReservationStatus.RESCHEDULED);

        return reservationMapper.toDetailDTO(reservationRepository.save(reservation));
    }
}