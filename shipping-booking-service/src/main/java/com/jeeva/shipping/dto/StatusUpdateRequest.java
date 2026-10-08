package com.jeeva.shipping.dto;

import com.jeeva.shipping.entity.BookingStatus;
import jakarta.validation.constraints.NotNull;

public record StatusUpdateRequest(@NotNull BookingStatus status) {
}
