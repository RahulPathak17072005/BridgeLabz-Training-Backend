package payroll.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * JPA Entity representing an authenticated system user.
 *
 * Maps to the "users" table in H2 in-memory database.
 * Stores login credentials (BCrypt-hashed password) and role assignment.
 *
 * Roles:
 *   ADMIN - full CRUD + payroll summary access
 *   USER  - read-only access to employees
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** Unique login identifier. Max 50 chars. */
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    /** BCrypt-hashed password. Stored as 60-char hash. */
    @Column(nullable = false, length = 64)
    private String password;

    /** Unique contact email. Max 100 chars. */
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    /** Access role: ADMIN or USER. */
    @Column(nullable = false, length = 20)
    private String role;
}
