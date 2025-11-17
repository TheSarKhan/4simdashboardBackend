package com.backend.dashboarddemo.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "dashboards")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Dashboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String name;

    @Column(nullable = false, length = 2000)
    String embedUrl;

    @Builder.Default
    boolean active = true;

    @ManyToMany(mappedBy = "dashboards", fetch = FetchType.LAZY)
    @Builder.Default
    Set<UserRole> roles = new HashSet<>();

}
