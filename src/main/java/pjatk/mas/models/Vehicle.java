package pjatk.mas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String licensePlate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType type;

    @ManyToOne
    @JoinColumn(name = "delivery_driver_id", nullable = false)
    private DeliveryDriver deliveryDriver;

    public Vehicle(String licensePlate, VehicleType type, DeliveryDriver deliveryDriver) {
        this.licensePlate = licensePlate;
        this.type = type;
        this.deliveryDriver = deliveryDriver;
    }

    public int getMaxOrderLoad() {
        return switch (type) {
            case BICYCLE -> 2;
            case MOTORBIKE -> 4;
            case CAR -> 10;
        };
    }
}
