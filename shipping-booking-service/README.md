# Shipping Booking Service

A **sanitized, original portfolio project** inspired by common shipping-booking workflows. It does not contain employer source code, internal URLs, credentials, database schemas, customer data, or proprietary business rules.

## Business scenario
A customer creates a shipment booking from one port to another, assigns a vessel and container quantity, retrieves the booking, updates details/status, and cancels the booking when allowed.

## Architecture
Client -> REST Controller -> Service -> Spring Data JPA Repository -> H2 Database

## Technology
- Java 17
- Spring Boot 3
- REST API
- Spring Data JPA
- Bean Validation
- H2 Database
- Maven
- JUnit / MockMvc
- Spring Boot Actuator
- Jenkins pipeline
- Docker

## Features
- Create shipment booking
- Retrieve one/all bookings
- Filter by status
- Update complete booking
- Partially update booking status
- Cancel booking
- Validation and global exception handling
- Health endpoint

## Run
```bash
mvn clean test
mvn spring-boot:run
```

Health check: `GET /actuator/health`
H2 console: `http://localhost:8080/h2-console`

## API examples

### Create
```bash
curl -X POST http://localhost:8080/api/bookings \
-H "Content-Type: application/json" \
-d '{"customerName":"Demo Customer","originPort":"INMAA","destinationPort":"SGSIN","vesselName":"Demo Vessel","containerCount":2}'
```

### Get all
```bash
curl http://localhost:8080/api/bookings
```

### Get by booking number
```bash
curl http://localhost:8080/api/bookings/BKG123456
```

### Full update
```bash
curl -X PUT http://localhost:8080/api/bookings/BKG123456 \
-H "Content-Type: application/json" \
-d '{"customerName":"Demo Customer","originPort":"INMAA","destinationPort":"CNSHA","vesselName":"Demo Vessel 2","containerCount":3}'
```

### Status update
```bash
curl -X PATCH http://localhost:8080/api/bookings/BKG123456/status \
-H "Content-Type: application/json" \
-d '{"status":"CONFIRMED"}'
```

### Cancel
```bash
curl -X DELETE http://localhost:8080/api/bookings/BKG123456
```

## FDE talking points
- End-to-end REST request flow
- Business validation in service layer
- Clear API contract using DTOs
- Centralized exception handling
- Health monitoring through Actuator
- CI build/test/package stages through Jenkins
- Container packaging through Docker
- The same service can later connect to PostgreSQL and deploy to a cloud VM
