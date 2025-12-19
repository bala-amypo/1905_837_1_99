package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "asset_lifecycle_events")
public class AssetLifecycleEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String eventDescription;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "logged_at", nullable = false)
    private LocalDateTime loggedAt;

    public AssetLifecycleEvent() {
        this.loggedAt = LocalDateTime.now();
    }

    public AssetLifecycleEvent(Asset asset, String eventType, String eventDescription, LocalDate eventDate) {
        this.asset = asset;
        this.eventType = eventType;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
        this.loggedAt = LocalDateTime.now();
    }
}
