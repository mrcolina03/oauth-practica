package com.example.author.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/authorized").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/api/authors", true)
                )
                .oauth2Client(oauth2 -> {})
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                );

        // Para H2 Console
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.clientRegistration());
    }

    private ClientRegistration clientRegistration() {
        return ClientRegistration.withRegistrationId("oauth-server")
                .clientId("client-app")
                .clientSecret("secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8081/login/oauth2/code/oauth-server")
                .scope("openid", "profile", "read", "write")
                .authorizationUri("http://localhost:9000/oauth2/authorize")
                .tokenUri("http://localhost:9000/oauth2/token")
                .userInfoUri("http://localhost:9000/userinfo")
                .jwkSetUri("http://localhost:9000/oauth2/jwks")
                .issuerUri("http://localhost:9000")
                .clientName("OAuth Server")
                .build();
    }
}