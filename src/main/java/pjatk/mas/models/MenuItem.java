package pjatk.mas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.math.RoundingMode;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "menu_items")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer preparationTime;

    @ManyToMany(mappedBy = "menuItems")
    private List<Chef> chefs = new ArrayList<>();

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IngredientQuantity> ingredientQuantities = new ArrayList<>();

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuItemQuantity> menuItemQuantities = new ArrayList<>();

    public MenuItem(String name, Integer preparationTime) {
        this.name = name;
        this.preparationTime = preparationTime;
    }

    @Transient
    public BigDecimal getPrice() {
        if (ingredientQuantities == null || ingredientQuantities.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return ingredientQuantities.stream()
                .map(iq -> iq.getIngredient().getBasePrice().multiply(iq.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public void addIngredient(Ingredient ingredient, BigDecimal quantity) {
        IngredientQuantity iq = new IngredientQuantity(quantity, this, ingredient);
        ingredientQuantities.add(iq);
        ingredient.getIngredientQuantities().add(iq);
    }
}