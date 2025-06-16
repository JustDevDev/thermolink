package org.skomi.pilot.shared.service;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.PilotApplication;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.core.Violations;

@UtilityClass
@Slf4j
public class ModulithValidator {

    /**
     * Validates the application's modular structure using the Modulith framework.
     * <p>
     * This method performs the validation of the application's modular architecture by:
     * - Retrieving the application modules associated with the `PilotApplication` class.
     * - Verifying the modular structure through the `ApplicationModules.verify()` method.
     * - Logging the violations (if any) encountered during the verification process and
     * exiting the application with a non-zero status code in case of validation failure.
     * - Printing the details of the validated modules to the standard output.
     * <p>
     * Throws:
     * - Logs any violations encountered during the verification process as error messages
     * and terminates the application in case of validation failure.
     */
    public static void validateModulith() {
        ApplicationModules modules = ApplicationModules.of(PilotApplication.class);
        try {
            modules.verify();
        } catch (Violations e) {
            log.error("Violations: ", e);
            System.exit(1);
        }
        modules.forEach(System.out::println);
    }
}
