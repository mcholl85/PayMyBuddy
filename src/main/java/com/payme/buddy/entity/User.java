package com.payme.buddy.entity;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String lastname;

    @NotNull
    private String firstname;

    private BigDecimal balance;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public void addAmountToBalance(BigDecimal amount) {
        this.setBalance(balance.add(amount));
    }

    public void removeAmountToBalance(BigDecimal amount) {
        this.setBalance(balance.subtract(amount));
    }
}
