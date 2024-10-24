package com.codecool.sv_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "memberships")
@Getter
@Setter
@NoArgsConstructor
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Stripe-related fields
    private String stripeCustomerId;
    private String stripeSubscriptionId;
    private String stripePaymentIntentId;
    private String clientSecret;

    // Subscription details
    private SubscriptionStatus subscriptionStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime updatedAt;

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
