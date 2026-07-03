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
@Table(name = "chefs")
@DiscriminatorValue("CHEF")
public class Chef extends Employee {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "chef_cooking_courses", joinColumns = @JoinColumn(name = "chef_id"))
    @Column(name = "course_name", nullable = false)
    private List<String> cookingCourses = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "chef_menu_item", joinColumns = @JoinColumn(name = "chef_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_item_id")
    )
    private List<MenuItem> menuItems = new ArrayList<>();

    public Chef(String name, String surname, BigDecimal salaryPerHour) {
        super(name, surname, salaryPerHour);
    }

    @Override
    public String getRole() {
        return "CHEF";
    }

    public void addCookingCourse(String course) {
        cookingCourses.add(course);
    }

    public void addMenuItem(MenuItem menuItem) {
        menuItems.add(menuItem);
        menuItem.getChefs().add(this);
    }
}