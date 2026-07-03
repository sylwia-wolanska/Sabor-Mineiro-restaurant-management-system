package pjatk.mas.models;

import jakarta.persistence.*;
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
@Table(name = "waiters")
@DiscriminatorValue("WAITER")
public class Waiter extends Employee {

    @ManyToMany
    @JoinTable(
            name = "waiter_dining_table",
            joinColumns = @JoinColumn(name = "waiter_id"),
            inverseJoinColumns = @JoinColumn(name = "dining_table_id")
    )
    private List<DiningTable> diningTables = new ArrayList<>();

    @OneToMany(mappedBy = "waiter")
    private List<Order> processedOrders = new ArrayList<>();

    public Waiter(String name, String surname, BigDecimal salaryPerHour) {
        super(name, surname, salaryPerHour);
    }

    @Override
    public String getRole() {
        return "WAITER";
    }

    public void addDiningTable(DiningTable table) {
        diningTables.add(table);
        table.getWaiters().add(this);
    }

    public void addProcessedOrder(Order order) {
        processedOrders.add(order);
        order.setWaiter(this);
    }
}