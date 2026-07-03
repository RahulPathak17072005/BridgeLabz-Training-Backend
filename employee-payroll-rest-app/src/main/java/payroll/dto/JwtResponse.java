package payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Immutable DTO wrapping the generated JWT token string.
 *
 * Serialized by Jackson to JSON:
 * {
 *   "jwttoken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
 * }
 *
 * Uses @Getter (not @Data) to keep this DTO truly immutable —
 * no setters are generated.
 */
@Getter
@AllArgsConstructor
public class JwtResponse {
    private final String jwttoken;
}
