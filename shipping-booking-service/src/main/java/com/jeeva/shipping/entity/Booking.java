package com.jeeva.shipping.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String bookingNumber;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String originPort;

    @Column(nullable = false)
    private String destinationPort;

    @Column(nullable = false)
    private String vesselName;

    @Column(nullable = false)
    private Integer containerCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Booking() {
    }

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // GETTERS

    public Long getId() {
        return id;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getOriginPort() {
        return originPort;
    }

    public String getDestinationPort() {
        return destinationPort;
    }

    public String getVesselName() {
        return vesselName;
    }

    public Integer getContainerCount() {
        return containerCount;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // SETTERS

    public void setId(Long id) {
        this.id = id;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setOriginPort(String originPort) {
        this.originPort = originPort;
    }

    public void setDestinationPort(String destinationPort) {
        this.destinationPort = destinationPort;
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public void setContainerCount(Integer containerCount) {
        this.containerCount = containerCount;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}