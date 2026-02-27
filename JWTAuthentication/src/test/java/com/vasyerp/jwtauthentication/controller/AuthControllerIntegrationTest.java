package com.vasyerp.jwtauthentication.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Test
    void loginShouldReturnJwtForValidCredentials() throws Exception {
        String requestBody = """
                {
                  "username": "admin",
                  "password": "1234"
                }
                """;

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").isNotEmpty())
                .andExpect(jsonPath("$.token_type").value("Bearer"))
                .andExpect(jsonPath("$.expires_in").value(900))
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andReturn();

        String token = extractAccessToken(result.getResponse().getContentAsString());
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertThat(claims.getSubject()).isEqualTo("admin");
        assertThat(claims.get("username", String.class)).isEqualTo("admin");
        assertThat(claims.get("role", String.class)).isEqualTo("ADMIN");
        assertThat(claims.get("issuedAt", Long.class)).isNotNull();
        assertThat(claims.get("expiration", Long.class)).isNotNull();
        assertThat(claims.getExpiration().getTime() - claims.getIssuedAt().getTime())
                .isEqualTo(900_000L);
    }

    @Test
    void loginShouldReturnUnauthorizedForInvalidCredentials() throws Exception {
        String requestBody = """
                {
                  "username": "admin",
                  "password": "wrong-password"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid username or password"));
    }

    @Test
    void protectedEndpointShouldReturnUnauthorizedWithoutBearerToken() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protectedEndpointShouldBeAccessibleWithValidBearerToken() throws Exception {
        String loginRequestBody = """
                {
                  "username": "admin",
                  "password": "1234"
                }
                """;

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestBody))
                .andExpect(status().isOk())
                .andReturn();

        String token = extractAccessToken(loginResult.getResponse().getContentAsString());

        mockMvc.perform(get("/")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    private String extractAccessToken(String responseBody) {
        Pattern tokenPattern = Pattern.compile("\"access_token\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = tokenPattern.matcher(responseBody);
        assertThat(matcher.find()).isTrue();
        return matcher.group(1);
    }
}
