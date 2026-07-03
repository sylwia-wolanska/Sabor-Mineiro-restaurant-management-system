package pjatk.mas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Min(1)
    @Column(nullable = false)
    private Integer countOfPeople;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus reservationStatus = ReservationStatus.ACCEPTED;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "dining_table_id", nullable = false)
    private DiningTable diningTable;

    public Reservation(LocalDateTime dateTime, Integer countOfPeople, Client client, DiningTable diningTable) {
        this.dateTime = dateTime;
        this.countOfPeople = countOfPeople;
        this.client = client;
        this.diningTable = diningTable;
        this.reservationStatus = ReservationStatus.ACCEPTED;
    }
}
