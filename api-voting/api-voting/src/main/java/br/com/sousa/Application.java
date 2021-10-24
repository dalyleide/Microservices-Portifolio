package br.com.sousa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
@EnableCaching
@ComponentScan
@EnableScheduling
@SpringBootApplication(scanBasePackages = "br.com.sousa.domain.service")
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
}
