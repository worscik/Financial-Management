package pl.financemanagement.ApplicationConfig;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");

        dataSource.setUrl("mysql://db:3306/financial?useSSL=false&allowPublicKeyRetrieval=true");
        // Replace 'financial' with your desired database name
        dataSource.setUsername("user");
        dataSource.setPassword("admin");
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);

    }

    @Bean
    public CommandLineRunner init(JdbcTemplate jdbcTemplate) {
        return args -> {
            jdbcTemplate.execute("CREATE DATABASE IF NOT EXISTS financial");
        };
    }

}
