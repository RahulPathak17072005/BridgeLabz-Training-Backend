package payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for login request bodies.
 *
 * Deserialized by Jackson from JSON payload:
 * {
 *   "username": "hr_kiran",
 *   "password": "Kiran@12"
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequest {
    private String username;
    private String password;
}
