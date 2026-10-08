package com.jeeva.shipping.service;

import com.jeeva.shipping.dto.*;
import com.jeeva.shipping.entity.*;
import com.jeeva.shipping.exception.BookingNotFoundException;
import com.jeeva.shipping.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class BookingService {
  private final BookingRepository repository;

  public BookingService(BookingRepository repository) {
    this.repository = repository;
  }

  public BookingResponse create(BookingRequest request) {
    if (request.originPort().equalsIgnoreCase(request.destinationPort()))
      throw new IllegalStateException("Origin and destination ports must be different");
    Booking booking = Booking.builder().bookingNumber(generateNumber()).customerName(request.customerName())
        .originPort(request.originPort().toUpperCase()).destinationPort(request.destinationPort().toUpperCase())
        .vesselName(request.vesselName()).containerCount(request.containerCount()).status(BookingStatus.CREATED)
        .build();
    return map(repository.save(booking));
  }

  @Transactional(readOnly = true)
  public List<BookingResponse> findAll(BookingStatus status) {
    List<Booking> rows = status == null ? repository.findAll() : repository.findByStatus(status);
    return rows.stream().map(this::map).toList();
  }

  @Transactional(readOnly = true)
  public BookingResponse findByNumber(String number) {
    return map(get(number));
  }

  public BookingResponse update(String number, BookingRequest request) {
    Booking b = get(number);
    ensureEditable(b);
    b.setCustomerName(request.customerName());
    b.setOriginPort(request.originPort().toUpperCase());
    b.setDestinationPort(request.destinationPort().toUpperCase());
    b.setVesselName(request.vesselName());
    b.setContainerCount(request.containerCount());
    return map(repository.save(b));
  }

  public BookingResponse updateStatus(String number, StatusUpdateRequest request) {
    Booking b = get(number);
    if (b.getStatus() == BookingStatus.CANCELLED || b.getStatus() == BookingStatus.COMPLETED)
      throw new IllegalStateException("Finalized booking status cannot be changed");
    b.setStatus(request.status());
    return map(repository.save(b));
  }

  public BookingResponse cancel(String number) {
    Booking b = get(number);
    ensureEditable(b);
    b.setStatus(BookingStatus.CANCELLED);
    return map(repository.save(b));
  }

  private Booking get(String n) {
    return repository.findByBookingNumber(n).orElseThrow(() -> new BookingNotFoundException("Booking not found: " + n));
  }

  private void ensureEditable(Booking b) {
    if (b.getStatus() == BookingStatus.CANCELLED || b.getStatus() == BookingStatus.COMPLETED)
      throw new IllegalStateException("Finalized booking cannot be modified");
  }

  private String generateNumber() {
    String n;
    do {
      n = "BKG" + ThreadLocalRandom.current().nextInt(100000, 999999);
    } while (repository.findByBookingNumber(n).isPresent());
    return n;
  }

  private BookingResponse map(Booking b) {
    return new BookingResponse(b.getId(), b.getBookingNumber(), b.getCustomerName(), b.getOriginPort(),
        b.getDestinationPort(), b.getVesselName(), b.getContainerCount(), b.getStatus(), b.getCreatedAt(),
        b.getUpdatedAt());
  }
}
