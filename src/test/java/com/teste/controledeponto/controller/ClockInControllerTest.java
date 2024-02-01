package com.teste.controledeponto.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.teste.controledeponto.dto.clockin.ClockinDTO;
import com.teste.controledeponto.dto.clockin.ClockinResponseDTO;
import com.teste.controledeponto.dto.jwt.JwtRequest;
import com.teste.controledeponto.model.User;
import com.teste.controledeponto.repository.ClockInRepository;
import com.teste.controledeponto.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class ClockInControllerTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
        "postgres:15-alpine"
    ).withExposedPorts(5433, 5432);

    @DynamicPropertySource
     static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    private ClockInRepository clockInRepository;

    @Autowired
    private UserRepository userRepository;

    @LocalServerPort
    int localPort;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    @DisplayName("save clockin when successfull")
    void clockin_Save_Successful() {
        clockInRepository.deleteAll();
        ClockinDTO clockinDTO = ClockinDTO.builder().dateTime(LocalDateTime.now().toString()).build();

        var url = String.format("http://localhost:%s/batidas", localPort);

        var clockinResponseDTO = restTemplate.exchange(
            url,
            HttpMethod.POST,
            new HttpEntity<>(clockinDTO, getAuthorizationHeader()),
            ClockinResponseDTO.class
        );

        Assertions.assertThat(clockinResponseDTO.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("Conflict when time already registered")
    void clockin_Conflict() {
        clockInRepository.deleteAll();
        ClockinDTO clockinDTO = ClockinDTO.builder().dateTime(LocalDateTime.now().toString()).build();

        var url = String.format("http://localhost:%s/batidas", localPort);

        ResponseEntity<ClockinResponseDTO> response = null;

        for (int i = 0; i < 2; i++) {

            response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(clockinDTO, getAuthorizationHeader()),
                ClockinResponseDTO.class
            );
        }

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("At least 1 hour of lunch time or exception is thrown")
    void clockin_OneHourLunch_ThrowsException() {
        clockInRepository.deleteAll();
        var clockinDTOS = List.of(
            ClockinDTO.builder().dateTime(LocalDateTime.now().minusHours(1).toString()).build(),
            ClockinDTO.builder().dateTime(LocalDateTime.now().minusMinutes(30).toString()).build(),
            ClockinDTO.builder().dateTime(LocalDateTime.now().toString()).build()
        );

        var url = String.format("http://localhost:%s/batidas", localPort);

        ResponseEntity<ClockinResponseDTO> response = null;

        for (ClockinDTO clockinDTO : clockinDTOS) {
            response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(clockinDTO, getAuthorizationHeader()),
                ClockinResponseDTO.class
            );
        }
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("Not allowed to clockin on saturday or sunday")
    void clockin_NoSundayNorSaturday() {
        clockInRepository.deleteAll();

        var lastSunday = LocalDateTime.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        var clockinDTO = ClockinDTO.builder()
            .dateTime(lastSunday.toString()).build();

        var url = String.format("http://localhost:%s/batidas", localPort);

        var responseEntity = restTemplate.exchange(
            url,
            HttpMethod.POST,
            new HttpEntity<>(clockinDTO, getAuthorizationHeader()),
            ClockinResponseDTO.class);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(responseEntity.getBody()).hasAllNullFieldsOrProperties();
    }


    @Test
    @DisplayName("Cannot clockin more than 4 times a day")
    void clockin_FourClockinsADay_ThorwsException() {
        clockInRepository.deleteAll();

        var clockinDTOS = List.of(
            ClockinDTO.builder().dateTime(LocalDateTime.now().minusHours(3).toString()).build(),
            ClockinDTO.builder().dateTime(LocalDateTime.now().minusHours(2).toString()).build(),
            ClockinDTO.builder().dateTime(LocalDateTime.now().minusHours(1).toString()).build(),
            ClockinDTO.builder().dateTime(LocalDateTime.now().minusMinutes(30).toString()).build(),
            ClockinDTO.builder().dateTime(LocalDateTime.now().toString()).build()
        );

        var url = String.format("http://localhost:%s/batidas", localPort);

        ResponseEntity<ClockinResponseDTO> response = null;

        for (ClockinDTO clockinDTO : clockinDTOS) {
            response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(clockinDTO, getAuthorizationHeader()),
                ClockinResponseDTO.class
            );
        }
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }


    public HttpHeaders getAuthorizationHeader() {
        var user = User.builder()
            .id(1L)
            .username("teste")
            .password(new BCryptPasswordEncoder().encode("pass"))
            .role("USER")
            .build();

        userRepository.save(user);

        ResponseEntity<JsonNode> jwtResponseResponseEntity = restTemplate.exchange("http://localhost:" + localPort + "/authenticate",
            HttpMethod.POST,
            new HttpEntity<>(new JwtRequest("teste", "pass")),
            JsonNode.class);

        var headers = new HttpHeaders();
        var jwtToken = jwtResponseResponseEntity.getBody()
            .findPath("jwtToken")
            .asText();

        headers.setBearerAuth(jwtToken);
        return headers;
    }


}