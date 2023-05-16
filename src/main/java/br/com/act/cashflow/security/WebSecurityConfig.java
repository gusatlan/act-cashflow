package br.com.act.cashflow.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {
    private final String jwtUri;

    public WebSecurityConfig(
            @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") final String jwtUri
    ) {
        this.jwtUri = jwtUri;
    }


    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        return http.authorizeExchange(exchanges ->
                        exchanges
                                .pathMatchers(
                                        "/item-cash-flow",
                                        "/item-cash-flow/**",
                                        "/cashflow",
                                        "/cashflow/**",
                                        "/report",
                                        "/report/**"
                                )
                                .authenticated()
                                .anyExchange()
                                .permitAll()
                )
                .csrf().disable()
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer
                                .jwt(jwt ->
                                        jwt.jwkSetUri(jwtUri)
                                )
                )
                .build();
    }

}
