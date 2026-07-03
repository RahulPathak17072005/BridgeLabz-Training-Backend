package payroll.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Request Filter — intercepts every HTTP request exactly once.
 *
 * Extends OncePerRequestFilter to guarantee single execution per request
 * even in filter chains with multiple registrations.
 *
 * Processing pipeline:
 *  1. Read the "Authorization" header.
 *  2. Extract "Bearer <token>" substring.
 *  3. Parse username from the token via JwtTokenUtil.
 *  4. Load UserDetails from the database via UserDetailsService.
 *  5. Validate token integrity and expiry.
 *  6. If valid → set authentication in SecurityContext.
 *  7. Continue the filter chain (pass to the next filter/controller).
 *
 * If any step fails (missing header, bad token, expired):
 *  → Skip authentication setup → Spring Security returns 401 automatically.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        // Step 1-2: Extract Bearer token from Authorization header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (Exception e) {
                logger.warn("JWT token parsing failed: " + e.getMessage());
            }
        }

        // Step 3-6: Validate token and set security context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                // Build authentication token with granted authorities
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // Attach HTTP request details (IP, session) to the token
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Place authentication into the SecurityContext for this request
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Step 7: Continue the filter chain regardless
        chain.doFilter(request, response);
    }
}
