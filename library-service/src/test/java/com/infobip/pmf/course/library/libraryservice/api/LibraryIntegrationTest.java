package com.infobip.pmf.course.library.libraryservice.api;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.BDDAssertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = "/prepare_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class LibraryIntegrationTest {
    @LocalServerPort
    private int port;
    private RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create("http://localhost:" + port);
    }

    @Test
    void shouldGetUnauthorizedException() {
        var exception = catchThrowableOfType(
                () -> restClient.get()
                        .uri("/libraries")
                        .header(HttpHeaders.AUTHORIZATION, "App invalid-api-key")
                        .retrieve()
                        .toEntity(String.class),
                HttpClientErrorException.class
        );

        then(exception.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldGetLibraryNotFoundException() {
        String invalidLibraryId = "99999";

        var exception = catchThrowableOfType(
                () -> restClient.get()
                        .uri("/libraries/" + invalidLibraryId)
                        .header(HttpHeaders.AUTHORIZATION, "App la9psd71atbpgeg7fvvx")
                        .retrieve()
                        .toEntity(String.class),
                HttpClientErrorException.class
        );

        then(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldTestFlow() throws JSONException {

        String newLibraryJson = """
        {
            "groupId": "org.springframework",
            "artifactId": "spring-core",
            "name": "Spring Core",
            "description": "Spring Core Framework"
        }
        """;

        String updateLibrary = """
        {
            "name": "Spring Core Framework",
            "description": "Spring Core Framework"
        }
        """;


        var response = restClient.post()
                .uri("/libraries")
                .contentType(MediaType.APPLICATION_JSON)
                .body(newLibraryJson)
                .header(HttpHeaders.AUTHORIZATION, "App la9psd71atbpgeg7fvvx")
                .retrieve()
                .toEntity(String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        JSONAssert.assertEquals("""
                {
                    "id": 4,
                    "groupId": "org.springframework",
                    "artifactId": "spring-core",
                    "name": "Spring Core",
                    "description": "Spring Core Framework",
                    "versions": []
                }""", response.getBody(), true);

        response = restClient.get()
                .uri("/libraries")
                .header(HttpHeaders.AUTHORIZATION, "App la9psd71atbpgeg7fvvx")
                .retrieve()
                .toEntity(String.class);

        JSONAssert.assertEquals("""
                {
                    results: [
                        {
                            "id": 1,
                            "groupId": "com.infobip",
                            "artifactId": "libraryA",
                            "name": "Library A",
                            "description": "Description for Library A",
                            "versions" : [1, 2, 3]
                        },
                        {
                            "id": 2,
                            "groupId": "com.infobip",
                            "artifactId": "libraryB",
                            "name": "Library B",
                            "description": "Description for Library B",
                            "versions" : [4, 5]
                        },
                        {
                            "id": 3,
                            "groupId": "com.infobip",
                            "artifactId": "libraryC",
                            "name": "Library C",
                            "description": "Description for Library C",
                            "versions" : [6]
                        },
                        {
                            "id": 4,
                            "groupId": "org.springframework",
                            "artifactId": "spring-core",
                            "name": "Spring Core",
                            "description": "Spring Core Framework",
                            "versions" : []
                        }
                    ],
                    "page": 0,
                    "size": 20,
                    "totalPages": 1,
                    "totalResults": 4
                }""", response.getBody(), true);

        response = restClient.patch()
                .uri("/libraries/1")
                .contentType(MediaType.APPLICATION_JSON)
                .body(updateLibrary)
                .header(HttpHeaders.AUTHORIZATION, "App la9psd71atbpgeg7fvvx")
                .retrieve()
                .toEntity(String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JSONAssert.assertEquals("""
                {
                            "id": 1,
                            "groupId": "com.infobip",
                            "artifactId": "libraryA",
                            "name": "Spring Core Framework",
                            "description": "Spring Core Framework",
                            "versions" : [1, 2, 3]
                }""", response.getBody(), true);

        response = restClient.delete()
                .uri("/libraries/2")
                .header(HttpHeaders.AUTHORIZATION, "App la9psd71atbpgeg7fvvx")
                .retrieve()
                .toEntity(String.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        response = restClient.get()
                .uri("/libraries")
                .header(HttpHeaders.AUTHORIZATION, "App la9psd71atbpgeg7fvvx")
                .retrieve()
                .toEntity(String.class);

        JSONAssert.assertEquals("""
                {
                    results: [
                        {
                            "id": 1,
                            "groupId": "com.infobip",
                            "artifactId": "libraryA",
                            "name": "Spring Core Framework",
                            "description": "Spring Core Framework",
                            "versions" : [1, 2, 3]
                        },
                        {
                            "id": 3,
                            "groupId": "com.infobip",
                            "artifactId": "libraryC",
                            "name": "Library C",
                            "description": "Description for Library C",
                            "versions" : [6]
                        },
                        {
                            "id": 4,
                            "groupId": "org.springframework",
                            "artifactId": "spring-core",
                            "name": "Spring Core",
                            "description": "Spring Core Framework",
                            "versions" : []
                        }
                    ],
                    "page": 0,
                    "size": 20,
                    "totalPages": 1,
                    "totalResults": 3
                }""", response.getBody(), true);

        var exception = catchThrowableOfType(
                () -> restClient.post()
                        .uri("/libraries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(newLibraryJson)
                        .header(HttpHeaders.AUTHORIZATION, "App la9psd71atbpgeg7fvvx")
                        .retrieve()
                        .toEntity(String.class),
                HttpClientErrorException.class
        );

        then(exception.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        exception = catchThrowableOfType(
                () -> restClient.get()
                        .uri("/libraries?page=-1&size=-1")
                        .header(HttpHeaders.AUTHORIZATION, "App la9psd71atbpgeg7fvvx")
                        .retrieve()
                        .toEntity(String.class),
                HttpClientErrorException.class
        );

        then(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldGetBadRequestForInvalidInput() {
        // given
        String invalidLibraryJson = """
                   {
                    "name": "NEW XO",
                    "location":"MARS"
                   }
                """;

        // when
        var exception = catchThrowableOfType(
                () -> restClient.post()
                        .uri("/libraries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "App la9psd71atbpgeg7fvvx")
                        .body(invalidLibraryJson)
                        .retrieve()
                        .toEntity(String.class),
                HttpClientErrorException.class
        );

        // then
        then(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldGetVersionNotFoundException() {
        String invalidVersionId = "99999";

        var exception = catchThrowableOfType(
                () -> restClient.get()
                        .uri("/libraries/1/versions/" + invalidVersionId)
                        .header(HttpHeaders.AUTHORIZATION, "App la9psd71atbpgeg7fvvx")
                        .retrieve()
                        .toEntity(String.class),
                HttpClientErrorException.class
        );

        then(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldGetBadRequestForInvalidInput2() throws JSONException {
        // given
        String invalidVersionJson = """
                   {
                    "description": "Major update",
                    "releaseDate": "2024-05-20T00:00:00",
                    "libraryId": 1
                   }
                """;

        // when
        var exception = catchThrowableOfType(
                () -> restClient.post()
                        .uri("/libraries/1/versions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "App la9psd71atbpgeg7fvvx")
                        .body(invalidVersionJson)
                        .retrieve()
                        .toEntity(String.class),
                HttpClientErrorException.class
        );

        // then
        then(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
