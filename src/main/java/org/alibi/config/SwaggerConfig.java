package org.alibi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Конфигурационный класс для настройки Swagger.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * Создает и возвращает Docket bean для настройки Swagger.
     *
     * @return Docket bean
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.alibi.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
