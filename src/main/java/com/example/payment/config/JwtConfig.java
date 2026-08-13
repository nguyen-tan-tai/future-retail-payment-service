package com.example.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

/**
 * JWT Configuration using RSA256 public key from auth-service JWKS endpoint.
 * No longer uses shared secret - validates JWT signatures using public key.
 */
@Configuration
public class JwtConfig {

    @Value("${app.security.auth-server-jwks-url:http://localhost:18081/oauth2/jwks}")
    private String jwksUrl;

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwksUrl).build();
    }
}
