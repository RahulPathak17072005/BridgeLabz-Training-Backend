package com.greet.config;

import com.greet.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();

        // Skip auth for registration, login, H2 Console, and Swagger UI / OpenAPI docs
        if (path.contains("/auth/") || path.contains("/h2-console") ||
                path.contains("/swagger-ui") || path.contains("/v3/api-docs") ||
                path.contains("/api-docs")) {
            return true;
        }

        // Allow pre-flight OPTIONS requests
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Blocked request to {}: Missing or invalid Bearer Token", path);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Missing or invalid Authorization header token\"}");
            return false;
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = jwtUtil.validateToken(token);
            String username = claims.getSubject();
            String role = claims.get("role", String.class);
            request.setAttribute("username", username);
            request.setAttribute("role", role);

            // Role check: Only ADMIN can perform writes (POST, PUT, DELETE)
            String method = request.getMethod();
            if (("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)
                    || "DELETE".equalsIgnoreCase(method))
                    && !path.contains("/auth/")) {
                if (!"ADMIN".equalsIgnoreCase(role)) {
                    log.warn("Blocked modification request to {} by USER '{}': Insufficient role privileges",
                            path, username);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"Access denied. Admin role required.\"}");
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            log.warn("Rejected access token exception: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid or expired JWT token\"}");
            return false;
        }
    }
}
