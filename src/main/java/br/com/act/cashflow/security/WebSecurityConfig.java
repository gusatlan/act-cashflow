package br.com.act.cashflow.security;

//@Configuration
//@EnableWebSecurity
//@EnableWebFluxSecurity
public class WebSecurityConfig {
    public static final String ADMIN = "admin";
    public static final String ACCOUNTING = "accounting";
    public static final String FINANCE = "finance";
//    private final JwtAuthConverter jwtAuthConverter;
//    private final HttpSecurity httpSecurity;

//    public WebSecurityConfig(
//            final JwtAuthConverter jwtAuthConverter
//            ,final HttpSecurity httpSecurity
//    ) {
//        this.jwtAuthConverter = jwtAuthConverter;
//        this.httpSecurity = httpSecurity;
//    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests()
//                .requestMatchers("/swagger-ui.html", "/swagger-ui/**").permitAll()
//                .requestMatchers("/cashflow", "/cashflow/**").hasAnyRole(ADMIN, FINANCE, ACCOUNTING)
//                .requestMatchers("/item-cash-flow", "/item-cash-flow/**").hasAnyRole(ADMIN, FINANCE, ACCOUNTING)
//                .requestMatchers(HttpMethod.GET, "/report").hasAnyRole(ADMIN, FINANCE, ACCOUNTING)
//                .anyRequest().authenticated();
//        http.oauth2ResourceServer()
//                .jwt()
//                .jwtAuthenticationConverter(jwtAuthConverter);
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        return http.build();
//    }

}
