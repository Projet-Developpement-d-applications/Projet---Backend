package projet.conquerants.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(getDatabaseUrl());
        dataSource.setUsername(getDatabaseUsername());
        dataSource.setPassword(getDatabasePassword());
        return dataSource;
    }

    private String getDatabaseUrl() {
        // Retrieve database URL from environment variable or configuration
        return "jdbc:" + System.getenv("MYSQLCONNSTR_DB_URL");
    }

    private String getDatabaseUsername() {
        // Retrieve database username from environment variable or configuration
        return System.getenv("DB_USERNAME");
    }

    private String getDatabasePassword() {
        // Retrieve database password from environment variable or configuration
        return System.getenv("DB_PASSWORD");
    }
}
