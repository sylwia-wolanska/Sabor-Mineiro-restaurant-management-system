package pjatk.mas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "delivery_drivers")
@DiscriminatorValue("DELIVERY_DRIVER")
public class DeliveryDriver extends Employee {

    @NotBlank
    @Column(nullable = false, unique = true)
    private String drivingLicenseNumber;

    @OneToMany(mappedBy = "deliveryDriver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vehicle> vehicles = new ArrayList<>();

    @OneToMany(mappedBy = "deliveryDriver")
    private List<DeliveryOrder> deliveryOrders = new ArrayList<>();

    public DeliveryDriver(String name, String surname, BigDecimal salaryPerHour, String drivingLicenseNumber) {
        super(name, surname, salaryPerHour);
        this.drivingLicenseNumber = drivingLicenseNumber;
    }

    @Override
    public String getRole() {
        return "DELIVERY_DRIVER";
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        vehicle.setDeliveryDriver(this);
    }

    public int getMaxNumOfOrdersAtOnce() {
        return vehicles.stream().mapToInt(Vehicle::getMaxOrderLoad).max().orElse(0);
    }
}