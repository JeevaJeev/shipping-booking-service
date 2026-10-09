package com.jeeva.shipping.service;

import com.jeeva.shipping.dto.BookingRequest;
import com.jeeva.shipping.dto.BookingResponse;
import com.jeeva.shipping.dto.StatusUpdateRequest;
import com.jeeva.shipping.entity.Booking;
import com.jeeva.shipping.entity.BookingStatus;
import com.jeeva.shipping.exception.BookingNotFoundException;
import com.jeeva.shipping.repository.BookingRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingService {

    private final BookingRepository repository;

    public BookingService(BookingRepository repository) {
        this.repository = repository;
    }

    /**
     * Get all bookings
     */
    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings() {

        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    /**
     * Filter bookings by status
     */
    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByStatus(
            BookingStatus status) {

        return repository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Create booking
     */
    public BookingResponse create(
        BookingRequest request) {

    if (request.originPort()
            .equalsIgnoreCase(
                    request.destinationPort())) {

        throw new IllegalStateException(
                "Origin and destination ports must be different");
    }

    Booking booking = new Booking();

    booking.setBookingNumber(generateNumber());
    booking.setCustomerName(request.customerName());
    booking.setOriginPort(request.originPort().toUpperCase());
    booking.setDestinationPort(request.destinationPort().toUpperCase());
    booking.setVesselName(request.vesselName());
    booking.setContainerCount(request.containerCount());
    booking.setStatus(BookingStatus.CREATED);

    return mapToResponse(
            repository.save(booking));
}

    /**
     * Find booking by booking number
     */
    @Transactional(readOnly = true)
    public BookingResponse findByNumber(
            String bookingNumber) {

        return mapToResponse(
                get(bookingNumber));
    }

    /**
     * Update booking
     */
    public BookingResponse update(
            String bookingNumber,
            BookingRequest request) {

        Booking booking = get(bookingNumber);

        ensureEditable(booking);

        booking.setCustomerName(
                request.customerName());

        booking.setOriginPort(
                request.originPort().toUpperCase());

        booking.setDestinationPort(
                request.destinationPort().toUpperCase());

        booking.setVesselName(
                request.vesselName());

        booking.setContainerCount(
                request.containerCount());

        return mapToResponse(
                repository.save(booking));
    }

    /**
     * Update booking status
     */
    public BookingResponse updateStatus(
            String bookingNumber,
            StatusUpdateRequest request) {

        Booking booking = get(bookingNumber);

        if (booking.getStatus() == BookingStatus.CANCELLED
                || booking.getStatus() == BookingStatus.COMPLETED) {

            throw new IllegalStateException(
                    "Finalized booking status cannot be changed");
        }

        booking.setStatus(request.status());

        return mapToResponse(
                repository.save(booking));
    }

    /**
     * Cancel booking
     */
    public BookingResponse cancel(
            String bookingNumber) {

        Booking booking;
        booking = get(bookingNumber);

        ensureEditable(booking);

        booking.setStatus(
                BookingStatus.CANCELLED);

        return mapToResponse(
                repository.save(booking));
    }

    /**
     * Java 8 Stream Examples
     */

    public long getConfirmedBookingCount() {

        return repository.findAll()
                .stream()
                .filter(booking ->
                        booking.getStatus()
                                == BookingStatus.CONFIRMED)
                .count();
    }

    public int getTotalContainerCount() {

        return repository.findAll()
                .stream()
                .mapToInt(
                        Booking::getContainerCount)
                .sum();
    }

    public List<String> getAllVesselNames() {

        return repository.findAll()
                .stream()
                .map(
                        Booking::getVesselName)
                .distinct()
                .sorted()
                .toList();
    }

    public List<BookingResponse> getLatestBookings() {

        return repository.findAll()
                .stream()
                .sorted(
                        Comparator.comparing(
                                Booking::getCreatedAt)
                                .reversed())
                .limit(5)
                .map(this::mapToResponse)
                .toList();
    }

    public Map<BookingStatus, Long>
    getBookingCountByStatus() {

        return repository.findAll()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Booking::getStatus,
                                Collectors.counting()));
    }

    /**
     * Private Methods
     */

    private Booking get(String bookingNumber) {

        return repository.findByBookingNumber(
                        bookingNumber)
                .orElseThrow(() ->
                        new BookingNotFoundException(
                                "Booking not found: "
                                        + bookingNumber));
    }

    private void ensureEditable(
            Booking booking) {

        if (booking.getStatus()
                == BookingStatus.CANCELLED
                || booking.getStatus()
                == BookingStatus.COMPLETED) {

            throw new IllegalStateException(
                    "Finalized booking cannot be modified");
        }
    }

    private String generateNumber() {

        String bookingNumber;

        do {

            bookingNumber =
                    "BKG" +
                    ThreadLocalRandom.current()
                            .nextInt(
                                    100000,
                                    999999);

        } while (repository
                .findByBookingNumber(
                        bookingNumber)
                .isPresent());

        return bookingNumber;
    }

    private BookingResponse mapToResponse(
            Booking booking) {

        return new BookingResponse(
                booking.getId(),
                booking.getBookingNumber(),
                booking.getCustomerName(),
                booking.getOriginPort(),
                booking.getDestinationPort(),
                booking.getVesselName(),
                booking.getContainerCount(),
                booking.getStatus(),
                booking.getCreatedAt(),
                booking.getUpdatedAt());
    }

	public List<BookingResponse> findAll(BookingStatus status) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'findAll'");
	}
}