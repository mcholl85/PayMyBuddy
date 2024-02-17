package com.payme.buddy.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "connexions")
public class Connexion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user1_id")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2_id")
    private User user2;
}
