package com.backend.dashboarddemo.model;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String description;
    Instant createdAt;

    @ManyToMany(mappedBy = "roles")
    @Builder.Default
    Set<User> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_dashboards",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "dashboard_id")
    )
    Set<Dashboard> dashboards = new HashSet<>();

    @PostConstruct
    void init() {
        createdAt = Instant.now();
    }
}
