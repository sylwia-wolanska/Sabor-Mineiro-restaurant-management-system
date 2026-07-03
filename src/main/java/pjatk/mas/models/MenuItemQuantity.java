package pjatk.mas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "menu_item_quantities",
        uniqueConstraints = {@UniqueConstraint(name = "uq_order_menu_item", columnNames = {"order_id", "menu_item_id"})}
)
public class MenuItemQuantity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    public MenuItemQuantity(Integer quantity, Order order, MenuItem menuItem) {
        this.quantity = quantity;
        this.order = order;
        this.menuItem = menuItem;
    }

    public BigDecimal getSubtotal() {
        return menuItem.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}