package com.jeeva.shipping.controller;

import com.jeeva.shipping.dto.*;
import com.jeeva.shipping.entity.BookingStatus;
import com.jeeva.shipping.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController{
    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> create(@Valid @RequestBody BookingRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));
    }

    @GetMapping
    public List<BookingResponse> all(@RequestParam(required = false) BookingStatus status) {
        return service.findAll(status);
    }

    @GetMapping("/{bookingNumber}")
    public BookingResponse one(@PathVariable String bookingNumber) {
        return service.findByNumber(bookingNumber);
    }

    @PutMapping("/{bookingNumber}")
    public BookingResponse update(@PathVariable String bookingNumber, @Valid @RequestBody BookingRequest r) {
        return service.update(bookingNumber, r);
    }

    @PatchMapping("/{bookingNumber}/status")
    public BookingResponse status(@PathVariable String bookingNumber, @Valid @RequestBody StatusUpdateRequest r) {
        return service.updateStatus(bookingNumber, r);
    }

    @DeleteMapping("/{bookingNumber}")
    public BookingResponse cancel(@PathVariable String bookingNumber) {
        return service.cancel(bookingNumber);
    }
}
