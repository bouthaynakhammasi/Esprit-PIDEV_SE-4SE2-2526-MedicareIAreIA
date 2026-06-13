package com.aziz.demosec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableScheduling;
import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class DemosecApplication {

    @PostConstruct
    public void init() {
        // Enforce application-wide timezone consistency to match DB and scheduled jobs
        TimeZone.setDefault(TimeZone.getTimeZone("Africa/Tunis"));
    }

    public static void main(String[] args) {
        SpringApplication.run(DemosecApplication.class, args);
    }

}
