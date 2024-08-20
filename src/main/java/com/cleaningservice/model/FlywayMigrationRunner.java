package com.cleaningservice.model;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class FlywayMigrationRunner implements ApplicationRunner {

    @Autowired
    private Flyway flyway;

    @Override
    public void run(ApplicationArguments args) {
        flyway.migrate();
    }
}
