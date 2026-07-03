package payroll.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import payroll.dto.JwtRequest;
import payroll.dto.JwtResponse;
import payroll.model.User;
import payroll.repository.UserRepository;
import payroll.security.JwtTokenUtil;

import java.util.Map;

/**
 * Authentication REST Controller.
 *
 * Base path: /api/auth
 * Public endpoints — no JWT token required.
 *
 * Endpoints:
 *   POST /api/auth/register  → Register a new user account
 *   POST /api/auth/login     → Authenticate and receive a JWT token
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User registration and JWT login endpoints")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ─── POST /api/auth/login ─────────────────────────────────────────────────

    /**
     * Authenticates a user and returns a signed JWT token.
     *
     * Process:
     *  1. Extract credentials from JwtRequest body.
     *  2. Delegate to AuthenticationManager (checks BCrypt password).
     *  3. Load UserDetails from database.
     *  4. Generate JWT via JwtTokenUtil.
     *  5. Return JwtResponse with the token string.
     *
     * On bad credentials → 401 Unauthorized.
     *
     * Example request:
     *   POST /api/auth/login
     *   { "username": "hr_kiran", "password": "Kiran@12" }
     *
     * Example response:
     *   { "jwttoken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." }
     */
    @Operation(summary = "Login with username and password to receive JWT token")
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest req) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "status", 401,
                            "error", "Unauthorized",
                            "message", "Invalid username or password"
                    ));
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(req.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    // ─── POST /api/auth/register ──────────────────────────────────────────────

    /**
     * Registers a new user account.
     *
     * Process:
     *  1. Check for duplicate username → 409 Conflict.
     *  2. Check for duplicate email → 409 Conflict.
     *  3. Hash the plain-text password with BCrypt.
     *  4. Persist the User entity to H2 database.
     *  5. Return 200 OK with success message.
     *
     * Example request:
     *   POST /api/auth/register
     *   {
     *     "username": "hr_kiran",
     *     "password": "Kiran@12",
     *     "email": "kiran@payroll.com",
     *     "role": "ADMIN"
     *   }
     *
     * Example response:
     *   "User registered successfully"
     */
    @Operation(summary = "Register a new user (ADMIN or USER role)")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {

        // Check duplicate username
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "status", 409,
                            "error", "Conflict",
                            "message", "Username '" + user.getUsername() + "' is already taken"
                    ));
        }

        // Check duplicate email
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "status", 409,
                            "error", "Conflict",
                            "message", "Email '" + user.getEmail() + "' is already registered"
                    ));
        }

        // Validate role
        String role = user.getRole();
        if (role == null || (!role.equals("ADMIN") && !role.equals("USER"))) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "status", 400,
                            "error", "Bad Request",
                            "message", "Role must be either 'ADMIN' or 'USER'"
                    ));
        }

        // Hash password before persisting
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }
}
