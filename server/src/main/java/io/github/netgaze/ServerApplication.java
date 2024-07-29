package io.github.netgaze;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "io.github.netgaze")
@OpenAPIDefinition(info = @Info(title = "NetGaze Server", version = "1.0", description = "NetGaze Server REST API"))
@EnableJpaRepositories(basePackages = "io.github.netgaze.*")
@EnableTransactionManagement
@ComponentScan(basePackages = { "io.github.netgaze.*" })
@EntityScan(basePackages = "io.github.netgaze.*")
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}