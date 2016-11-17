package io.pivotal.poc;

import io.pivotal.poc.config.SSLValidationDisabler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResourceApplication {

    public static void main(String[] args) {
        if ("true".equals(System.getenv("SKIP_SSL_VALIDATION"))) {
            SSLValidationDisabler.disableSSLValidation();
        }
        SpringApplication.run(ResourceApplication.class, args);
    }
}
