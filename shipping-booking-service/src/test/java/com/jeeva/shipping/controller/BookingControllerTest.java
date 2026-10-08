package com.jeeva.shipping.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc
class BookingControllerTest {
 @Autowired MockMvc mvc; @Autowired ObjectMapper mapper;
 @Test void createsBooking() throws Exception {
   String json="""{"customerName":"Demo Customer","originPort":"INMAA","destinationPort":"SGSIN","vesselName":"Demo Vessel","containerCount":2}""";
   mvc.perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated()).andExpect(jsonPath("$.bookingNumber").exists()).andExpect(jsonPath("$.status").value("CREATED"));
 }
}
