package pjatk.mas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pickup_order")
@DiscriminatorValue("PICKUP")
public class PickupOrder extends Order {

    @NotNull
    @Column(nullable = false)
    private LocalTime hourOfPickup;

    public PickupOrder(Client client, LocalTime hourOfPickup) {
        super(client);
        this.hourOfPickup = hourOfPickup;
    }
}