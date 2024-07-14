package org.alibi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Конфигурационный класс для настройки базы данных.
 */
@Configuration
@ComponentScan(basePackages = {"org.alibi"})
public class DatabaseConfig {

    /**
     * Создает и возвращает DataSource для подключения к базе данных.
     *
     * @return DataSource bean
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5434/postgres");
        dataSource.setUsername("alibi");
        dataSource.setPassword("alibi");
        return dataSource;
    }

    /**
     * Создает и возвращает JdbcTemplate для выполнения SQL-запросов.
     *
     * @param dataSource источник данных
     * @return JdbcTemplate bean
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
