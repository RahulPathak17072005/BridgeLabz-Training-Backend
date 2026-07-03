package com.greet.filter;
import com.greet.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();

        boolean publicUrl =
                uri.endsWith("/login") ||
                        uri.endsWith("/register") ||
                        uri.contains("/css/") ||
                        uri.contains("/js/");

        if (publicUrl) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);

        if (session == null ||
                session.getAttribute("currentUser") == null) {

            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User currentUser =
                (User) session.getAttribute("currentUser");

        boolean adminOnly =
                uri.contains("/greetings/new") ||
                        uri.contains("/greetings/edit") ||
                        uri.contains("/greetings/delete");

        if (adminOnly &&
                !"ADMIN".equals(currentUser.getRole())) {

            res.sendRedirect(
                    req.getContextPath() + "/unauthorized");
            return;
        }

        chain.doFilter(request, response);
    }
}