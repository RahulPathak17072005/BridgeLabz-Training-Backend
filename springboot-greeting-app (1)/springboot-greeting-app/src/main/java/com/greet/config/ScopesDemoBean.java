package com.greet.config;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import java.util.UUID;
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode =
        ScopedProxyMode.TARGET_CLASS)
public class ScopesDemoBean {

    private final String requestId = UUID.randomUUID().toString();

    public String getRequestId() {
        return this.requestId;
    }
}