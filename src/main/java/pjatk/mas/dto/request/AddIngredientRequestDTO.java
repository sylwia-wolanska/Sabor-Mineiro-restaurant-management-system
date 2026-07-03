package pjatk.mas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddIngredientRequestDTO {

    @NotNull
    private Long ingredientId;

    @NotNull
    private java.math.BigDecimal quantity;
}