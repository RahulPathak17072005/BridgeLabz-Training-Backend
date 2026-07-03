package payroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import payroll.model.User;

import java.util.Optional;

/**
 * Spring Data JPA repository for User entity.
 *
 * Extends JpaRepository to inherit standard CRUD methods:
 *   save(), findById(), findAll(), deleteById(), count(), etc.
 *
 * Spring automatically generates the SQL implementation at runtime
 * from the method name via query derivation.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by their unique login username.
     * Derived query → SELECT * FROM users WHERE username = ?
     *
     * Used by:
     *  - UserDetailsService (Spring Security authentication)
     *  - AuthController (duplicate username check on register)
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a username is already taken.
     * Derived query → SELECT COUNT(*) > 0 FROM users WHERE username = ?
     */
    boolean existsByUsername(String username);

    /**
     * Checks if an email is already registered.
     * Derived query → SELECT COUNT(*) > 0 FROM users WHERE email = ?
     */
    boolean existsByEmail(String email);
}
