package io.labforward.api.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by: Robert Wilson
 * Date:  18/04/2021
 * Project: api
 * Package: io.labforward.api.configurations
 * Class: SwaggerConfig
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig
{

    @Bean
    public Docket postsApi()
    {
        return new Docket(DocumentationType.SWAGGER_2)

            .select()
            .apis(RequestHandlerSelectors.basePackage("io.labforward.api"))
            .paths(PathSelectors.any())
            .build()
            .genericModelSubstitutes(ResponseEntity.class)
            .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo()
    {
        return new ApiInfoBuilder()
            .title("Science Lab Library API")
            .description("Science Lab Library API documentation for Developers")
            .termsOfServiceUrl("https://labforward.com/terms")
            .contact(new Contact("Labforward", "https://labforward.com", "developers@labforward.com"))
            .license("Labforward")
            .licenseUrl("developers@labforward.com")
            .version("1.0").build();
    }
}
