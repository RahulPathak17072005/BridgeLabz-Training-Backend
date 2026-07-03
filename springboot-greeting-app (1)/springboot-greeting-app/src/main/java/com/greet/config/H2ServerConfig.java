package com.greet.config;
import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.sql.SQLException;
@Configuration
public class H2ServerConfig {
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2TcpServer() throws SQLException {
        // Starts a TCP server on port 9092 allowing external clients to connect.
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort",
                "9092");
    }
}
