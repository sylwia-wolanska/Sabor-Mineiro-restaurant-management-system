package pjatk.mas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "persons")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "person_type", discriminatorType = DiscriminatorType.STRING)
public abstract class   Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String surname;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    public Person(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public abstract String getRole();
}