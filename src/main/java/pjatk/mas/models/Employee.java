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
@Table(name = "employees")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "employee_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("EMPLOYEE")
public abstract class Employee extends Person {

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal salaryPerHour;

    public Employee(String name, String surname, BigDecimal salaryPerHour) {
        super(name, surname);
        this.salaryPerHour = salaryPerHour;
    }
}