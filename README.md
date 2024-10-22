# CyberRangeCZ Platform Adaptive Training
This project represents back-end for managing adaptive training in CyberRangeCZ Platform.

# Repository Structure

The repository is structured with packages as follows:

### .api
Objects that are used for communication between the front-end and the back-end.

- model definitions for transferred and received DTOs.
- mappers
- validation

### .definition
Objects used for defining internal working of the application.

- annotations for swagger documentation
- configuration of frameworks and libraries
- exceptions

### .persistence
Objects used for communication with the database.

- entities and their enums
- repositories

### .rest
Objects used for handling HTTP requests.

- controllers
- facades
- transaction and security annotations

### .service
Objects used for business logic.

### .startup
Startup hooks.



* Swagger for local environment is available [here](http://localhost:8080/adaptive-training/api/v1/swagger-ui.html#/)
* H2 in-memory DB can be accessed [here](http://localhost:8080/adaptive-training/api/v1/h2-console)
