package com.aurionpro.BankingApp.entity;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long customerId;

    @Column(nullable = false, unique = true)
    @Email(message = "Invalid email format")
    @Pattern(
    	    regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
    	    message = "Email must be a valid format (e.g., user@example.com)"
    	)
    @ToString.Include
    private String email;

    @Column(nullable = false)
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Contact number must be a valid 10-digit mobile starting with 6-9")
    private String contactNo;

    @Column(nullable = false)
    @Past(message = "DOB must be in the past")
    private LocalDate dob;

    @PrePersist
    @PreUpdate
    private void validateAge() {
        int age = Period.between(this.dob, LocalDate.now()).getYears();
        if (age < 18) {
            throw new IllegalArgumentException("Customer must be at least 18 years old");
        }
    }

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @ToString.Exclude
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "address_id", nullable = false, referencedColumnName = "addressId")
    @ToString.Exclude
    private Address address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Account> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Transaction> transactions = new ArrayList<>();
}
