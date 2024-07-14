package org.alibi.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Конфигурационный класс для настройки Web MVC.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "org.alibi")
public class WebConfig implements WebMvcConfigurer {
}
