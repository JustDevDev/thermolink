package org.skomi.pilot;

import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.shared.service.ModulithValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulithic;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@Modulithic
@SpringBootApplication
@EnableScheduling
public class PilotApplication {

    public static void main(String[] args) {
        SpringApplication.run(PilotApplication.class, args);

        ModulithValidator.validateModulith();
    }
}
