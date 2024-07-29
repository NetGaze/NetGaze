package io.github.netgaze;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "io.github.netgaze")
@EnableJpaRepositories(basePackages = "io.github.netgaze.repository")
@OpenAPIDefinition(info = @Info(title = "NetGaze Server", version = "1.0", description = "NetGaze Server REST API"))
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}