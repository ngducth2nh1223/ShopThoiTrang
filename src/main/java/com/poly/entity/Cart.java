package com.poly.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "CART")
@Getter @Setter @NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartId")
    private Integer cartId;

    @Column(name = "userId")
    private Integer userId;

    @Column(name = "status")
    private String status;

    @Column(name = "CreatedAt")
    private LocalDateTime createAt;

    @Column(name = "UpdatedAt")
    private LocalDateTime updateAt;

    @PrePersist
    void onCreate() {
        createAt = LocalDateTime.now();
        updateAt = createAt;
    }

    @PreUpdate
    void onUpdate() {
        updateAt = LocalDateTime.now();
    }
}