package pjatk.mas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "restaurant_order")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "order_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus = OrderStatus.INITIALIZED;

    @Column
    private LocalDateTime cancelledAt;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "waiter_id")
    private Waiter waiter;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuItemQuantity> menuItemQuantities = new ArrayList<>();

    public Order(Client client) {
        this.client = client;
        this.orderStatus = OrderStatus.INITIALIZED;
    }

    @Transient
    public BigDecimal getTotalPrice() {
        return menuItemQuantities.stream().map(MenuItemQuantity::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Integer countEstimatedPrepTime() {
        return menuItemQuantities.stream()
                .map(miq -> miq.getMenuItem().getPreparationTime())
                .max(Integer::compareTo)
                .orElse(0);
    }

    public void addMenuItemQuantity(MenuItem menuItem, int quantity) {
        MenuItemQuantity miq = new MenuItemQuantity(quantity, this, menuItem);
        menuItemQuantities.add(miq);
    }

    public boolean isPickup() {
        return this instanceof PickupOrder;
    }
}