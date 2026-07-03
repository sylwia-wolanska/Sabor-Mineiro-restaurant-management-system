package pjatk.mas.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "managers")
@DiscriminatorValue("MANAGER")
public class Manager extends Employee {

    public Manager(String name, String surname, BigDecimal salaryPerHour) {
        super(name, surname, salaryPerHour);
    }

    @Override
    public String getRole() {
        return "MANAGER";
    }
}