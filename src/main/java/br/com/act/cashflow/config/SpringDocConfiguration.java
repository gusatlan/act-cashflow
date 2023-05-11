package br.com.act.cashflow.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;

//@Configuration
//@io.swagger.v3.oas.annotations.security.SecurityScheme(
//    name = "bearerAuth",
//    type = SecuritySchemeType.HTTP,
//    bearerFormat = "JWT",
//    scheme = "bearer"
//)
public class SpringDocConfiguration {

    private BuildProperties buildInfo;

    public SpringDocConfiguration(final BuildProperties buildInfo) {
        this.buildInfo = buildInfo;
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .packagesToScan("br.com.act.cashflow.controllers")
                .group("cashflow-microservice")
                .build();
    }

    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .components(
                        new Components().addSecuritySchemes(
                                "bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                        )
                )
                .info(
                        new Info().title("Acquire API")
                                .version(buildInfo.getVersion())
                );
    }

}
