package pjatk.mas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "ingredient")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull
    @DecimalMin("0.0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IngredientQuantity> ingredientQuantities = new ArrayList<>();

    public Ingredient(String name, BigDecimal basePrice) {
        this.name = name;
        this.basePrice = basePrice;
    }
}
