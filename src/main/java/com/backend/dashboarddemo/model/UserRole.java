package com.backend.dashboarddemo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    Long id;
    String name;
    String description;
    Instant createdAt;

    @ManyToMany(mappedBy = "roles")
    @Builder.Default
    @JsonBackReference(value = "user-roles")
    Set<User> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_dashboards",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "dashboard_id")
    )
    @JsonManagedReference(value = "role-dashboards")
    Set<Dashboard> dashboards = new HashSet<>();

    @PrePersist
    void init() {
        createdAt = Instant.now();
    }
}
