package com.github.heisdanielade.pamietampsa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Setter
@Table
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pet_seq")
    @SequenceGenerator(name = "pet_seq", sequenceName = "pet_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String species;
    private String breed;
    private LocalDate birthDate;

    @Transient
    private Integer age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser owner;


    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    public Pet(String name, String species, String breed, LocalDate birthDate){
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.birthDate = birthDate;
    }

    public Integer getAge(){
        return LocalDate.now().getYear() - this.birthDate.getYear();
    }
}
