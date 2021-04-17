package com.lickhunter.web.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        @PropertySource(value = "classpath:database.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "file:database.properties", ignoreResourceNotFound = true)
})
@ConfigurationProperties
@Getter
@Setter
public class DatabaseProperties {

    private DB db;
    private Jooq jooq;

    public class DB {
        private String url;
        private String username;
        private String password;
    }

    public class Jooq {
        private Sql sql;

        public class Sql {
            private String dialect;
        }
    }
}
