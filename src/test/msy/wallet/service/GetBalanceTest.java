package msy.wallet.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msy.wallet.WalletApplication;
import com.msy.wallet.exception.ErrorCode;
import com.msy.wallet.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = WalletApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GetBalanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    private User user;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/wallet";
        user = new User()
                .setId(1L)
                .setBalance(100L);
    }

    //test_1
    @Test
    void givenValidUserId_whenGetBalance_thenReturnCorrectBalance() {
        String url = baseUrl + "/getBalance?userId=" + user.getId();
        ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user.getBalance(), response.getBody().getBalance());
    }

    //test_2
    @Test
    void givenNotExistsUserId_whenGetBalance_thenReturnUserNotFoundException() {
        String url = baseUrl + "/getBalance?userId=" + 3;
        try {
            ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
        } catch (HttpClientErrorException e)
        {
            String responseBody = e.getResponseBodyAsString();
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(responseBody);
                    String errorCode = jsonNode.get("errorCode").asText();

                    assertEquals(Integer.valueOf(errorCode), ErrorCode.USER_NOT_FOUND.getCode());
                } catch (Exception ex) {
                    System.err.println("Error parsing response: " + ex.getMessage());
                }
        }
    }

    //test_3
    @Test
    void givenNullUserId_whenGetBalance_thenReturnUserNotFoundException() {
        String url = baseUrl + "/getBalance?userId=";
        try {
            ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
        } catch (HttpClientErrorException e)
        {
            String responseBody = e.getResponseBodyAsString();
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                String errorCode = jsonNode.get("errorCode").asText();

                assertEquals(Integer.valueOf(errorCode), ErrorCode.PARAMETER_IS_REQUIRED.getCode());
            } catch (Exception ex) {
                e.printStackTrace();
            }
        }
    }

    //test_4
    @Test
    void givenNoUserId_whenGetBalance_thenReturnUserNotFoundException() {
        String url = baseUrl + "/getBalance";
        try {
            ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
        } catch (HttpClientErrorException e)
        {
            String responseBody = e.getResponseBodyAsString();
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                String errorCode = jsonNode.get("errorCode").asText();

                assertEquals(Integer.valueOf(errorCode), ErrorCode.PARAMETER_IS_REQUIRED.getCode());
            } catch (Exception ex) {
                e.printStackTrace();
            }
        }
    }

    //test_5
    @Test
    void givenInvalidUserId_whenGetBalance_thenReturnUserNotFoundException() {
        String url = baseUrl + "/getBalance?userId=" + "a";
        try {
            ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
        } catch (HttpClientErrorException e)
        {
            String responseBody = e.getResponseBodyAsString();
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                String errorCode = jsonNode.get("errorCode").asText();

                assertEquals(Integer.valueOf(errorCode), ErrorCode.INVALID_INPUT.getCode());
            } catch (Exception ex) {
                e.printStackTrace();
            }
        }
    }
}

