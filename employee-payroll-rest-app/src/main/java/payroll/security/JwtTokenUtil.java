package payroll.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT Utility Component.
 *
 * Responsibilities:
 *  1. Generate a signed JWT token on successful login.
 *  2. Extract claims (username, expiry) from an incoming token.
 *  3. Validate token integrity and expiry for each secured request.
 *
 * Algorithm: HMAC-SHA256 (HS256)
 * Signing key: derived from jwt.secret in application.properties
 * Token expiry: 86400 seconds (24 hours) by default
 */
@Component
public class JwtTokenUtil {

    /** Secret key loaded from .env → application.properties. */
    @Value("${jwt.secret}")
    private String secret;

    /** Token lifetime in seconds (default: 86400 = 24 hours). */
    @Value("${jwt.expiration}")
    private Long expiration;

    // ─── Key Generation ──────────────────────────────────────────────────────

    /**
     * Builds a cryptographic HMAC-SHA Key from the raw secret string.
     * JJWT 0.11.x requires Key objects, not raw strings, for signing.
     */
    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ─── Token Generation ────────────────────────────────────────────────────

    /**
     * Generates a JWT token for an authenticated user.
     *
     * Token payload (claims):
     *   sub  → username
     *   iat  → issued-at timestamp
     *   exp  → expiry timestamp (iat + expiration seconds)
     *
     * @param userDetails Spring Security user details
     * @return signed JWT string
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return buildToken(claims, userDetails.getUsername());
    }

    private String buildToken(Map<String, Object> claims, String subject) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expiration * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ─── Token Parsing ───────────────────────────────────────────────────────

    /**
     * Extracts all claims from a token by verifying the signature.
     * Throws JwtException if signature is invalid or token is malformed.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Generic claim extractor using a Function resolver.
     * Allows extracting any claim (subject, expiry, etc.) cleanly.
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /** Extracts the username (subject) from the token payload. */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /** Extracts the expiration Date from the token payload. */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // ─── Token Validation ────────────────────────────────────────────────────

    /** Returns true if the token has passed its expiration timestamp. */
    private Boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    /**
     * Validates the token against the authenticated user.
     *
     * Checks:
     *  1. Username in token matches the UserDetails subject.
     *  2. Token has not expired.
     *
     * @param token       JWT string from Authorization header
     * @param userDetails loaded Spring Security user
     * @return true if token is valid for this user
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
