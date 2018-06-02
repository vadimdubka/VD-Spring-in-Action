package com.vadimdubka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
public class DataConfig {
    //TODO сделать один конфигурационный файл для DataSource и сконфигурировать там 2 DataSource для 2 частей приложения
    //TODO added from feature2
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                   .setType(EmbeddedDatabaseType.H2)
                   .addScript("schema.sql")
                   .addScript("insert-data.sql")
                   .build();
    }
    
    @Bean
    public JdbcOperations jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
