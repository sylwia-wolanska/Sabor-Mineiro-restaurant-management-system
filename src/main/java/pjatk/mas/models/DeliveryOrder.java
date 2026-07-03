package pjatk.mas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "delivery_order")
@DiscriminatorValue("DELIVERY")
public class DeliveryOrder extends Order {

    @NotBlank
    @Column(nullable = false)
    private String street;

    @NotBlank
    @Column(nullable = false)
    private String buildingNumber;

    @Column(nullable = true)
    private Integer apartmentNumber;

    @ManyToOne
    @JoinColumn(name = "delivery_driver_id")
    private DeliveryDriver deliveryDriver;

    public DeliveryOrder(Client client, String street, String buildingNumber, Integer apartmentNumber) {
        super(client);
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.apartmentNumber = apartmentNumber;
    }

    public String getFullAddress() {
        if (apartmentNumber != null) {
            return street + " " + buildingNumber + "/" + apartmentNumber;
        }
        return street + " " + buildingNumber;
    }
}
