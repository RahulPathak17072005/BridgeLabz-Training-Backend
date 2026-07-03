package com.greet;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootGreetingApplication {
    public static void main(String[] args) {
        // Load environment variables from .env if present in current directory
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );

        // Load environment variables from .env inside subdirectory if run from parent directory
        Dotenv dotenvSub = Dotenv.configure()
                .directory("./springboot-greeting-app")
                .ignoreIfMissing()
                .load();
        dotenvSub.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );

        SpringApplication.run(SpringBootGreetingApplication.class, args);
    }
}