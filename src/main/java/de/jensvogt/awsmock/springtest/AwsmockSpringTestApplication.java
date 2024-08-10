package de.jensvogt.awsmock.springtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"de.jensvogt.awsmock.springtest"})
public class AwsmockSpringTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(AwsmockSpringTestApplication.class, args);
    }

}
