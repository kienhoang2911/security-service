package com.example.securityservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.util.UUID;

@EnableWebSecurity
public class SecurityConfig {

    /**
     * First will be applied the OAuth2 security filters configuration.
     * In this configuration, I only indicate that all the failing request will be redirected to the /login page.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http
                // Redirect to the login page when not authenticated from the
                // authorization endpoint
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(
                                new LoginUrlAuthenticationEntryPoint("/login"))
                )
        ;

        return http.build();
    }

    /**
     * Then, will be applied the rest of the security filters.
     * Here, I indicate that all the endpoints require authentication and which is the login form.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                // Form login handles the redirect to the login page from the
                // authorization server filter chain
                .formLogin(Customizer.withDefaults());
        return http.build();
    }

    /**
     * Configuration of the OAuth2 client.
     * Multiple clients can be configured. The current implementation stores them in memory, but a table
     * in the database can be used to store all the registered clients.
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                // client-id and client-secret that must be used from all the OAuth2 clients
                .clientId("2e053c4d697e9887dca2")
                .clientSecret("c51e5160d2bdd5ab774d7dd1a40d919809c8e19c")
                // the Basic authentication method will be used between backend-gateway-client and backend-auth
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                // grant types to be used
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                // permitted redirect URI after the authentication is successful
                .redirectUri("http://localhost:8086/login/oauth2/code/github")
//                .redirectUri("http://backend-gateway-client:8080/authorized")
                // acceptable scopes for the authorization
//                .scope(OidcScopes.OPENID)
//                .scope("message.read")
//                .scope("message.write")
                .build();

        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    /**
     * Acceptable URL of the authorization server
     */
    @Bean
    public AuthorizationServerSettings providerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer("http://backend-auth:8086")
                .build();
    }
}
