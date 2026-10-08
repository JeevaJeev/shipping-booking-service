package com.jeeva.shipping.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

  @PrePersist
  void prePersist() {
    createdAt = LocalDateTime.now();
    updatedAt = createdAt;
  }

  @PreUpdate
  void preUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
