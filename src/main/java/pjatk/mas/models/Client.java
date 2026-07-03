package pjatk.mas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pjatk.mas.services.OrderService;
import pjatk.mas.services.ReservationService;
import pjatk.mas.session.OrderWizardSession;
import pjatk.mas.session.ReservationWizardSession;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "clients")
@DiscriminatorValue("CLIENT")
public class Client extends Person {

    @NotBlank
    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String emailAddress;

    @OneToMany(mappedBy = "client", orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "client", orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    public Client(String name, String surname, String phoneNumber, String emailAddress) {
        super(name, surname);
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    @Override
    public String getRole() {
        return "CLIENT";
    }

    public Order makeOrder(OrderWizardSession session, OrderService orderService) {
        session.setClientId(this.getId());
        return orderService.createOrder(session, this);
    }

    public Reservation makeReservation(ReservationWizardSession session, ReservationService reservationService) {
        session.setClientId(this.getId());
        return reservationService.createReservation(session, this);
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }
}