package pjatk.mas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
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
        name = "ingredient_quantities",
        uniqueConstraints = {@UniqueConstraint(name = "uq_menu_item_ingredient", columnNames = {"menu_item_id", "ingredient_id"})}
)
public class IngredientQuantity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal quantity;

    @ManyToOne
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @ManyToOne
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    public IngredientQuantity(BigDecimal quantity, MenuItem menuItem, Ingredient ingredient) {
        this.quantity = quantity;
        this.menuItem = menuItem;
        this.ingredient = ingredient;
    }
}