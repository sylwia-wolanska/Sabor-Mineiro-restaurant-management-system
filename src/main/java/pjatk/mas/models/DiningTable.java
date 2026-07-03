package pjatk.mas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "dining_table")
public class DiningTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer maxPeople;

    @ManyToMany(mappedBy = "diningTables")
    private List<Waiter> waiters = new ArrayList<>();

    @OneToMany(mappedBy = "diningTable")
    private List<Reservation> reservations = new ArrayList<>();

    public DiningTable(Integer maxPeople) {
        this.maxPeople = maxPeople;
    }

    public boolean isOccupied() {
        return reservations.stream().anyMatch(r -> r.getReservationStatus() == ReservationStatus.ONGOING);
    }
}
