package msy.wallet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msy.wallet.WalletApplication;
import com.msy.wallet.exception.ErrorCode;
import com.msy.wallet.model.Transaction;
import com.msy.wallet.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = WalletApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AddMoneyTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    private Transaction transaction;

    private User user;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/wallet";
        user = new User()
                .setId(1L)
                .setBalance(100L);

        transaction = new Transaction()
                .setId(1L)
                .setUser(user)
                .setAmount(500L)
                .setReferenceId("REF123");
    }

    @Test
    void givenValidUserId_whenAddMoney_thenReturnTransaction() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Transaction> response = restTemplate.postForEntity(
                baseUrl + "/addMoney?userId=1&amount=50",
                requestEntity, Transaction.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getReferenceId());
    }

    @Test
    void givenInsufficientAmount_whenAddMoney_thenReturnInsufficientException() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Transaction> response = restTemplate.postForEntity(
                    baseUrl + "/addMoney?userId=1&amount=-500",
                    requestEntity, Transaction.class);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
        }catch (HttpClientErrorException e) {
            String responseBody = e.getResponseBodyAsString();
            try {
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                String errorCode = jsonNode.get("errorCode").asText();

                assertEquals(Integer.valueOf(errorCode), ErrorCode.INSUFFICIENT_BALANCE.getCode());
            } catch (Exception ex) {
                System.err.println("Error parsing response: " + ex.getMessage());
            }
        }
    }

    @Test
    public void givenTwoConcurrentWithdrawals_whenAddMoney_thenOnlyOneWithdrawalSuccedAndAnotherOneGetOptimisticError() throws InterruptedException, JsonProcessingException {
        Long userId = 1L;
        Long withdrawalAmount = -80L;
        int threadCount = 2;

        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        Runnable task = () -> {
            try {
                String url = "/wallet/addMoney?userId=" + userId + "&amount=" + withdrawalAmount;
                ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);

                System.out.println("Thread " + Thread.currentThread().getName() + " -> Status: " + response.getStatusCode());

                if (response.getStatusCode() == HttpStatus.OK) {
                    JsonNode jsonNode = objectMapper.readTree(response.getBody());
                    assertNotNull(jsonNode.get("referenceId"), "referenceId should be present in the response");
                } else {
                    assertEquals(HttpStatus.CONFLICT, response.getStatusCode(), "Second request should fail due to optimistic locking");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        };

        for (int i = 0; i < threadCount; i++) {
            executor.execute(task);
        }

        latch.await();
        executor.shutdown();

        String balanceUrl = "/wallet/getBalance?userId=" + userId;
        ResponseEntity<String> balanceResponse = restTemplate.getForEntity(balanceUrl, String.class);
        assertEquals(HttpStatus.OK, balanceResponse.getStatusCode());

        JsonNode balanceNode = objectMapper.readTree(balanceResponse.getBody());
        assertNotNull(balanceNode.get("balance"), "Balance should be present in response");

        long finalBalance = balanceNode.get("balance").asLong();
        assertEquals(20, finalBalance, "Final balance should be 20 after one successful withdrawal");

        System.out.println("Final Balance: " + finalBalance);
    }
}

