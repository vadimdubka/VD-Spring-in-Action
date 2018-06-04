package com.vadimdubka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

//TODO сделать один конфигурационный файл для DataSource и сконфигурировать там 2 DataSource для 2 частей приложения
@Configuration
public class DataConfig {
    /** Configure DataSource */
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                   .setType(EmbeddedDatabaseType.H2)
                   .addScript("schema.sql")
                   .addScript("insert-data.sql")
                   .build();
    }
    
    /** Configure repository Template that hides boilerplate code from DAO classes. */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    @Bean
    public NamedParameterJdbcTemplate jdbcTemplate2(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
    
}
