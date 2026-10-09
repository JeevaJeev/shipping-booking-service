package com.jeeva.shipping.repository;

import com.jeeva.shipping.entity.Booking;
import com.jeeva.shipping.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface BookingRepository extends JpaRepository<Booking, Long> {
  Optional<Booking> findByBookingNumber(String bookingNumber);

  List<Booking> findByStatus(BookingStatus status);
}