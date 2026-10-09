# Shipping Booking Service

Shipping Booking Service is a Spring Boot backend application built around a practical shipment-booking workflow. I created this project to demonstrate how I design REST APIs, apply business validations, handle errors, test application behaviour, monitor service health, and prepare an application for automated build and container-based deployment.

This is an independently developed portfolio project inspired by general shipping and logistics workflows. It does not contain source code, internal URLs, credentials, production data, database structures, customer information, or proprietary business rules from any employer or client.

## Business Use Case

The application manages the basic lifecycle of a shipment booking. A customer can:

- Create a booking between an origin and destination port
- Assign a vessel and specify the number of containers
- Retrieve one or all bookings
- Filter bookings by status
- Update booking information
- Change the booking status
- Cancel an eligible booking

The service also applies business rules. For example, the origin and destination ports cannot be the same, and completed or cancelled bookings cannot be modified.

## Application Architecture

The project follows a standard layered Spring Boot architecture:

```text
Client / Postman
       |
       v
REST Controller
       |
       v
Service Layer
       |
       v
Repository Layer
       |
       v
H2 Database
```

### Request Flow

```text
HTTP Request
     |
     v
BookingController
     |
     v
BookingService
     |
     v
BookingRepository
     |
     v
H2 Database
     |
     v
JSON Response
```

The controller handles HTTP requests and responses. The service layer contains the business rules, while the repository layer manages database operations using Spring Data JPA.

## Technology Stack

- Java 17
- Spring Boot 3
- Spring Web
- REST APIs
- Spring Data JPA
- Jakarta Bean Validation
- H2 Database
- Maven
- JUnit and MockMvc
- Spring Boot Actuator
- Jenkins Pipeline
- Docker
- Git and GitHub

## Project Structure

```text
src/main/java/com/jeeva/shipping
|
|-- controller
|   `-- BookingController.java
|
|-- dto
|   |-- BookingRequest.java
|   |-- BookingResponse.java
|   `-- StatusUpdateRequest.java
|
|-- entity
|   |-- Booking.java
|   `-- BookingStatus.java
|
|-- exception
|   |-- BookingNotFoundException.java
|   |-- ErrorResponse.java
|   `-- GlobalExceptionHandler.java
|
|-- repository
|   `-- BookingRepository.java
|
|-- service
|   `-- BookingService.java
|
`-- ShippingBookingApplication.java
```

## Main Features

- Create shipment bookings
- Retrieve all bookings
- Retrieve a booking by booking number
- Filter bookings by status
- Update complete booking information
- Update only the booking status
- Cancel eligible bookings
- Validate incoming API requests
- Apply business rules in the service layer
- Return consistent error responses
- Monitor application health through Spring Boot Actuator
- Run automated tests with JUnit and MockMvc
- Build and package the application with Maven
- Run build and test stages through Jenkins
- Package the application as a Docker image

## Booking Status Lifecycle

The application supports the following statuses:

```text
CREATED
CONFIRMED
IN_TRANSIT
COMPLETED
CANCELLED
```

A normal booking follows this lifecycle:

```text
CREATED -> CONFIRMED -> IN_TRANSIT -> COMPLETED
    |
    `-> CANCELLED
```

A booking may be cancelled before it reaches a finalized state. Once a booking is completed or cancelled, the application prevents further modifications.

## Prerequisites

Install the following tools before running the project:

- Java 17
- Maven
- Git
- Docker Desktop, if Docker execution is required

Verify the installations:

```bash
java -version
mvn -version
git --version
docker --version
```

## Build and Run

### Run automated tests

```bash
mvn clean test
```

### Package the application

```bash
mvn clean package
```

The generated JAR file will be available in the `target` directory.

### Start the application

```bash
mvn spring-boot:run
```

The application starts on port `8080`.

## Application Monitoring

Spring Boot Actuator provides basic operational monitoring.

### Health check

```http
GET http://localhost:8080/actuator/health
```

Expected response:

```json
{
  "status": "UP"
}
```

### Application metrics

```http
GET http://localhost:8080/actuator/metrics
```

These endpoints help confirm that the service is available before checking application logs, database connectivity, or downstream integrations.

## H2 Database Console

The application uses an in-memory H2 database for local development and demonstration.

Open the console at:

```text
http://localhost:8080/h2-console
```

Use the datasource details configured in `application.properties`.

Typical local configuration:

```text
JDBC URL: jdbc:h2:mem:shippingdb
Username: sa
Password:
```

Because H2 is configured as an in-memory database, stored data may be cleared when the application restarts.

## REST API Endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| `POST` | `/api/bookings` | Create a new shipment booking |
| `GET` | `/api/bookings` | Retrieve all bookings |
| `GET` | `/api/bookings?status=CONFIRMED` | Filter bookings by status |
| `GET` | `/api/bookings/{bookingNumber}` | Retrieve one booking |
| `PUT` | `/api/bookings/{bookingNumber}` | Update the complete booking |
| `PATCH` | `/api/bookings/{bookingNumber}/status` | Update only the booking status |
| `DELETE` | `/api/bookings/{bookingNumber}` | Cancel an eligible booking |

## API Examples

### Create a booking

```bash
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Demo Customer",
    "originPort": "INMAA",
    "destinationPort": "SGSIN",
    "vesselName": "Demo Vessel",
    "containerCount": 2
  }'
```

Example response:

```json
{
  "id": 1,
  "bookingNumber": "BKG123456",
  "customerName": "Demo Customer",
  "originPort": "INMAA",
  "destinationPort": "SGSIN",
  "vesselName": "Demo Vessel",
  "containerCount": 2,
  "status": "CREATED"
}
```

The actual booking number is generated by the application.

### Retrieve all bookings

```bash
curl http://localhost:8080/api/bookings
```

### Filter bookings by status

```bash
curl "http://localhost:8080/api/bookings?status=CONFIRMED"
```

### Retrieve a booking by booking number

```bash
curl http://localhost:8080/api/bookings/BKG123456
```

Replace `BKG123456` with the booking number returned by the create API.

### Update a complete booking

```bash
curl -X PUT http://localhost:8080/api/bookings/BKG123456 \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Demo Customer",
    "originPort": "INMAA",
    "destinationPort": "CNSHA",
    "vesselName": "Demo Vessel 2",
    "containerCount": 3
  }'
```

### Update only the booking status

```bash
curl -X PATCH http://localhost:8080/api/bookings/BKG123456/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "CONFIRMED"
  }'
```

### Cancel a booking

```bash
curl -X DELETE http://localhost:8080/api/bookings/BKG123456
```

## Validation and Error Handling

The application validates requests before processing them. The validation rules include:

- Customer name is required
- Origin port is required
- Destination port is required
- Vessel name is required
- Container count must be greater than zero
- Origin and destination ports must be different

Centralized exception handling is implemented using:

```java
@RestControllerAdvice
@ExceptionHandler
```

This keeps error handling separate from the controller and provides consistent responses for:

- Invalid request data
- Booking not found
- Unsupported status changes
- Attempts to modify finalized bookings
- Business-rule validation failures

## Jenkins CI Pipeline

The repository includes a `Jenkinsfile` that defines a basic CI pipeline:

```text
GitHub Repository
       |
       v
Jenkins Checkout
       |
       v
Maven Build
       |
       v
Automated Tests
       |
       v
Application Package
       |
       v
JAR Artifact
```

The pipeline is designed to:

1. Check out the source code from GitHub
2. Compile the application
3. Run automated tests
4. Package the Spring Boot application
5. Archive the generated JAR artifact

This demonstrates how source code moves from GitHub through automated build, test, and packaging stages.

## Docker Support

The repository contains a `Dockerfile` for packaging the application into a Docker image.

### Build the application

```bash
mvn clean package
```

### Build the Docker image

```bash
docker build -t shipping-booking-service .
```

### Run the container

```bash
docker run --name shipping-booking-service \
  -p 8080:8080 \
  shipping-booking-service
```

### Verify the container

```bash
docker ps
```

### Check the health endpoint

```bash
curl http://localhost:8080/actuator/health
```

### Stop the container

```bash
docker stop shipping-booking-service
```

On Windows, Docker Desktop must be running before Docker commands can communicate with the Docker Engine.

## Configuration and Deployment Knowledge

Along with backend development, this project demonstrates my understanding of:

- Spring Boot application setup
- Maven dependency management
- Maven build and package lifecycle
- Application property configuration
- H2 datasource configuration
- REST endpoint configuration
- Request validation
- Spring Boot Actuator configuration
- Git and GitHub source-code management
- Jenkins pipeline setup
- Automated build and test stages
- Docker image configuration
- Port mapping between a container and host
- Environment-independent application packaging
- JAR artifact generation

## FDE-Relevant Experience Demonstrated

I designed this project to represent the type of end-to-end problem-solving expected from a Forward Deployed Engineer. It demonstrates the ability to:

- Understand a customer-facing business requirement
- Convert the requirement into a working backend service
- Design clear API contracts
- Implement the complete request-to-database flow
- Apply business rules and validations
- Handle failures with meaningful error responses
- Test and monitor the application
- Manage source code using Git and GitHub
- Configure automated build and test stages
- Package an application for consistent deployment
- Explain the complete delivery flow to technical and non-technical stakeholders

## End-to-End Delivery Flow

```text
Business Requirement
        |
        v
Java and Spring Boot Development
        |
        v
Local Testing
        |
        v
Git Commit
        |
        v
GitHub Push
        |
        v
Jenkins Build and Test
        |
        v
Maven Package
        |
        v
Docker Image
        |
        v
Application Container
        |
        v
Actuator Health Monitoring
```

## Future Improvements

Possible future enhancements include:

- PostgreSQL integration
- Swagger/OpenAPI documentation
- JWT authentication
- Role-based authorization
- External vessel schedule integration
- Email or notification service integration
- Kafka-based booking events
- Docker Compose
- Cloud deployment
- Centralized logging
- Distributed tracing
- Separate booking and notification microservices

## Portfolio Disclaimer

This is an independently developed portfolio project based on general shipping and logistics concepts.

All source code, sample data, APIs, database structures, and business rules in this repository were created specifically for demonstration purposes. The project does not reproduce or disclose confidential source code, credentials, production data, internal URLs, customer information, or proprietary implementation details belonging to any employer or client.