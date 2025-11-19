package com.backend.dashboarddemo.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String fullName;
    @Column(unique = true, nullable = false)
    String email;
    @Column(nullable = false)
    String password;
    String phoneNumber;
    boolean active;
    Instant createdAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<UserRole> roles = new HashSet<>();

    @PrePersist
    void init() {
        createdAt = Instant.now();
    }

    public CustomUserDetails customUserDetails() {
        return new CustomUserDetails(this);
    }
}
