package com.jeeva.shipping.dto;

import com.jeeva.shipping.entity.BookingStatus;
import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        String bookingNumber,
        String customerName,
        String originPort,
        String destinationPort,
        String vesselName,
        Integer containerCount,
        BookingStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
