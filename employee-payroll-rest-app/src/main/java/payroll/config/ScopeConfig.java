package payroll.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.annotation.RequestScope;
import payroll.dto.EmployeeDTO;

/**
 * Demonstrates Spring Bean Scope concepts using EmployeeDTO.
 *
 * ─── Scope Comparison ────────────────────────────────────────
 *
 * @Scope("singleton") [DEFAULT]
 *   → One instance for the entire application lifetime.
 *   → Shared across all requests and threads.
 *   → Use for stateless services (repositories, services).
 *
 * @Scope("prototype")
 *   → New instance created EVERY time the bean is requested.
 *   → Not shared. Each injection point gets a fresh copy.
 *   → Use for stateful objects (builders, DTOs).
 *
 * @RequestScope (= @Scope("request"))
 *   → New instance created per HTTP request.
 *   → Destroyed when the request completes.
 *   → Ideal for per-request state (user context, form data).
 *
 * These beans are demonstrated here for educational comparison.
 * In production, DTOs are typically instantiated via @RequestBody,
 * not injected as Spring beans.
 */
@Configuration
public class ScopeConfig {

    /**
     * Prototype-scoped EmployeeDTO.
     * A fresh EmployeeDTO is created each time this bean is injected.
     */
    @Bean
    @Scope("prototype")
    public EmployeeDTO prototypeEmployeeDto() {
        return new EmployeeDTO();
    }

    /**
     * Request-scoped EmployeeDTO.
     * One EmployeeDTO instance lives for the duration of each HTTP request.
     * Spring uses a proxy to inject this into singleton beans safely.
     */
    @Bean
    @RequestScope
    public EmployeeDTO requestScopedEmployeeDto() {
        return new EmployeeDTO();
    }
}
