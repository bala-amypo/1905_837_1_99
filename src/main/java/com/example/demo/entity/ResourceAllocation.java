package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resource_allocations")
public class ResourceAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    @OneToOne
    @JoinColumn(name = "resource_request_id", nullable = false, unique = true)
    private ResourceRequest resourceRequest;

    @Column(name = "allocated_at", nullable = false)
    private LocalDateTime allocatedAt;

    @Column(name = "conflict_flag", nullable = false)
    private Boolean conflictFlag;

    @Column
    private String notes;

    public ResourceAllocation() {
    }

    public ResourceAllocation(Resource resource, ResourceRequest resourceRequest, Boolean conflictFlag, String notes) {
        this.resource = resource;
        this.resourceRequest = resourceRequest;
        this.conflictFlag = conflictFlag;
        this.notes = notes;
    }

    @PrePersist
    public void prePersist() {
        this.allocatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Resource getResource() {
        return resource;
    }

    public ResourceRequest getResourceRequest() {
        return resourceRequest;
    }

    public LocalDateTime getAllocatedAt() {
        return allocatedAt;
    }

    public Boolean getConflictFlag() {
        return conflictFlag;
    }

    public String getNotes() {
        return notes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public void setResourceRequest(ResourceRequest resourceRequest) {
        this.resourceRequest = resourceRequest;
    }

    public void setAllocatedAt(LocalDateTime allocatedAt) {
        this.allocatedAt = allocatedAt;
    }

    public void setConflictFlag(Boolean conflictFlag) {
        this.conflictFlag = conflictFlag;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
