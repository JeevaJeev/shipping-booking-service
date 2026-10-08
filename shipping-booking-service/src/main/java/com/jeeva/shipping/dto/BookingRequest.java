package com.jeeva.shipping.dto;

import jakarta.validation.constraints.*;

public record BookingRequest(
        @NotBlank String customerName,
        @NotBlank @Size(min = 3, max = 5) String originPort,
        @NotBlank @Size(min = 3, max = 5) String destinationPort,
        @NotBlank String vesselName,
        @NotNull @Min(1) @Max(100) Integer containerCount) {
}
