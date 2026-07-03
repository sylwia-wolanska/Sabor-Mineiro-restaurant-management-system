package pjatk.mas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class CreateEmployeeRequestDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal salaryPerHour;

    @NotBlank
    private String role;

    private String drivingLicenseNumber;
}