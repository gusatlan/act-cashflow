package br.com.act.cashflow.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
//@EnableWebSecurity
@EnableWebFluxSecurity
public class WebSecurityConfig {
    public static final String ADMIN = "admin";
    public static final String ACCOUNTING = "accounting";
    public static final String FINANCE = "finance";
    private final JwtAuthConverter jwtAuthConverter;

    public WebSecurityConfig(final JwtAuthConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers( "/swagger-ui.html", "/swagger-ui/**").permitAll()
                .requestMatchers("/cashflow", "/cashflow/**").hasAnyRole(ADMIN, FINANCE, ACCOUNTING)
                .requestMatchers("/item-cash-flow", "/item-cash-flow/**").hasAnyRole(ADMIN, FINANCE, ACCOUNTING)
                .requestMatchers(HttpMethod.GET, "/report").hasAnyRole(ADMIN, FINANCE, ACCOUNTING)
                .anyRequest().authenticated();
        http.oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthConverter);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }

}
