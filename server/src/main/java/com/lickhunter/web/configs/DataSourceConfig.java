package com.lickhunter.web.configs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteOpenMode;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Value("${spring.datasource.driver-class-name}")
    private String driverName;
    @Value("${spring.datasource.url}")
    private String url;

    /**
     *
     * @return DataSource
     */
    @Bean("masterDataSource")
    @ConfigurationProperties("spring.datasource.master")
    public DataSource masterDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driverName);
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setMaximumPoolSize(1);
        hikariConfig.setConnectionTestQuery("SELECT 1");
        SQLiteConfig config= new SQLiteConfig();
        config.setOpenMode(SQLiteOpenMode.OPEN_URI);
        config.setOpenMode(SQLiteOpenMode.FULLMUTEX);
        config.setBusyTimeout("10000");
        hikariConfig.setPoolName("springHikariCP");
        hikariConfig.addDataSourceProperty(SQLiteConfig.Pragma.OPEN_MODE.pragmaName, config.getOpenModeFlags());
        hikariConfig.addDataSourceProperty(SQLiteConfig.Pragma.JOURNAL_MODE.pragmaName, SQLiteConfig.JournalMode.WAL );
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        return dataSource;
    }
}
