package com.codecool.sv_server.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codecool.sv_server.dto.PaymentIntentDto;
import com.codecool.sv_server.dto.SubscriptionReqDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Rollback
public class StripeControllerIT extends BaseIntegrationTest {

    @Autowired private EntityManager entityManager;

    private String token;
    private Long userId;

    private final String email = "stripe@payment.test";
    private final String password = "Password1";
    private final String name = "Stripe Test";

    @BeforeEach
    public void setup() throws Exception {
        registerUser(email, name, password);
        JsonNode loginResponse = loginUser(email, password);
        token = loginResponse.get("token").asText();
        userId = loginResponse.get("userId").asLong();
    }

    @AfterEach
    @Transactional
    public void cleanupDatabase() {
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM memberships").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM user_details").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM users").executeUpdate();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    @Test
    public void test_valid_stripe_request() throws Exception {
        var dto = new SubscriptionReqDto(userId, true, "good_happy_intent");
        var json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(
                        post("/api/stripe/payment-status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void test_payment_status_with_missing_auth_should_fail() throws Exception {
        var dto = new SubscriptionReqDto(userId, true, "test_intent");
        var json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(
                        post("/api/stripe/payment-status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test_payment_status_with_failed_payment_should_fail() throws Exception {
        var dto = new SubscriptionReqDto(userId, false, "test_intent");
        var json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(
                        post("/api/stripe/payment-status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content(json))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test_payment_status_with_duplicate_should_fail() throws Exception {
        var dto = new SubscriptionReqDto(userId, true, "test_intent");
        var json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(
                        post("/api/stripe/payment-status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content(json))
                .andExpect(status().isOk());

        mockMvc.perform(
                        post("/api/stripe/payment-status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_create_payment_intent_stripe_failure() throws Exception {
        try (MockedStatic<PaymentIntent> mocked = mockStatic(PaymentIntent.class)) {
            mocked.when(() -> PaymentIntent.create(any(PaymentIntentCreateParams.class)))
                    .thenThrow(new RuntimeException("Stripe down"));

            var dto = new PaymentIntentDto(null, null, 1L);
            var json = objectMapper.writeValueAsString(dto);

            mockMvc.perform(
                            post("/api/stripe/create-payment-intent")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + token)
                                    .content(json))
                    .andExpect(status().is5xxServerError());
        }
    }

    @Test
    public void test_create_payment_intent_valid_stripe() throws Exception {
        try (MockedStatic<PaymentIntent> mocked = mockStatic(PaymentIntent.class)) {
            PaymentIntent mockedPaymentIntent = mock(PaymentIntent.class);
            when(mockedPaymentIntent.getId()).thenReturn("pi_123456789");
            when(mockedPaymentIntent.getAmount())
                    .thenReturn(5000L); 
            when(mockedPaymentIntent.getCurrency()).thenReturn("usd");

            mocked.when(() -> PaymentIntent.create(any(PaymentIntentCreateParams.class)))
                    .thenReturn(mockedPaymentIntent);

            var dto = new PaymentIntentDto("pi_123456789", "pi_123456789", 1L);
            var json = objectMapper.writeValueAsString(dto);

            mockMvc.perform(
                            post("/api/stripe/create-payment-intent")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + token)
                                    .content(json))
                    .andExpect(status().isOk());
        }
    }
}
