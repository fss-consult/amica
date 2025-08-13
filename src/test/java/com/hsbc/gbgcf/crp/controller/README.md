# Questionnaire Controller Integration Tests

This directory contains integration tests for the QuestionnaireController class. These tests verify that the controller correctly interacts with the service layer and properly handles HTTP requests and responses.

## Test Structure

The integration tests are implemented in the `QuestionnaireControllerIntegrationTest` class. This class uses Spring's `@WebMvcTest` annotation to load only the web layer of the application, which is faster than loading the entire application context.

The tests use:
- `MockMvc` to simulate HTTP requests
- `@MockBean` to mock the QuestionnaireService
- Mockito to define the behavior of the mocked service

## Test Cases

The following endpoints are tested:

1. `GET /api/v1/form-data/{journeyType}/{customerIdentificationId}`
   - Tests successful retrieval of ODS data

2. `PUT /api/v1/saveODSData`
   - Tests successful saving of ODS data
   - Tests error handling when the service throws an exception

3. `POST /api/v1/submitODSData`
   - Tests successful submission of ODS data
   - Tests error handling when the service throws an exception

4. `GET /api/v1/viewForm`
   - Tests successful form viewing
   - Tests error handling when the service throws an exception

5. `GET /api/v1/reset`
   - Tests successful retrieval of the initial form
   - Tests error handling when the service throws an exception

6. `POST /api/v1/retake`
   - Tests successful questionnaire retake
   - Tests error handling when the service throws an exception

7. `GET /api/v1/questionnaire-error/{journeyType}/{customerIdentificationId}`
   - Tests successful retrieval of questionnaire error information
   - Tests handling of non-success responses

## Configuration

The tests use a custom `TestConfig` class that provides:
- A character encoding filter to ensure proper UTF-8 encoding in test responses

## Running the Tests

These tests can be run as part of the regular test suite using Maven:

```bash
mvn test
```

Or specifically for this test class:

```bash
mvn test -Dtest=QuestionnaireControllerIntegrationTest
```