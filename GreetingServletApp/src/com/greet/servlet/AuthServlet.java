package com.greet.servlet;

import com.greet.model.User;
import com.greet.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;

public class AuthServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        var context = WebApplicationContextUtils
                .getRequiredWebApplicationContext(getServletContext());

        this.userService = context.getBean(UserService.class);
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getRequestURI()
                .substring(request.getContextPath().length());

        if (path.equals("/login")) {

            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                    .forward(request, response);

        } else if (path.equals("/register")) {

            request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                    .forward(request, response);

        } else if (path.equals("/logout")) {

            HttpSession session = request.getSession(false);

            if (session != null) {
                session.invalidate();
            }

            response.sendRedirect(request.getContextPath() + "/login");

        } else if (path.equals("/unauthorized")) {

            request.getRequestDispatcher("/WEB-INF/views/unauthorized.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getRequestURI()
                .substring(request.getContextPath().length());

        if (path.equals("/login")) {

            String uName = request.getParameter("username");
            String pWord = request.getParameter("password");

            User authenticated = userService.login(uName, pWord);

            if (authenticated != null) {

                HttpSession session = request.getSession(true);
                session.setAttribute("currentUser", authenticated);

                response.sendRedirect(
                        request.getContextPath() + "/greetings");

            } else {

                request.setAttribute("error",
                        "Invalid username or password");

                request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                        .forward(request, response);
            }

        } else if (path.equals("/register")) {

            User newUser = new User();
            newUser.setUsername(request.getParameter("username"));
            newUser.setPassword(request.getParameter("password"));
            newUser.setEmail(request.getParameter("email"));
            newUser.setRole(request.getParameter("role"));

            if (userService.register(newUser)) {

                request.setAttribute("success",
                        "Registration successful. Please log in.");

                request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                        .forward(request, response);

            } else {

                request.setAttribute("error",
                        "Username is already taken");

                request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                        .forward(request, response);
            }
        }
    }
}