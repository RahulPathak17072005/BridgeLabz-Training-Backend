package com.greet.servlet;

import com.greet.model.Greeting;
import com.greet.service.GreetingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;

import java.io.IOException;

public class GreetingServlet extends HttpServlet {

    private GreetingService greetingService;
    private ClassPathXmlApplicationContext context;

    @Override
    public void init() throws ServletException {

        String xmlPath =
                getServletContext()
                        .getRealPath("/WEB-INF/applicationContext.xml");

        context =
                new ClassPathXmlApplicationContext(
                        "file:" + xmlPath);

        greetingService =
                (GreetingService) context.getBean("greetingService");

        System.out.println("=== GreetingServlet initialized ===");
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        String savedName = null;

        Cookie[] cookies =
                request.getCookies();

        if (cookies != null) {

            for (Cookie cookie : cookies) {

                if ("greetUserName".equals(
                        cookie.getName())) {

                    savedName =
                            cookie.getValue();

                    break;
                }
            }
        }

        request.setAttribute(
                "savedName",
                savedName);
        HttpSession session =
                request.getSession(false);

        if (session != null) {

            String lastUser =
                    (String) session.getAttribute(
                            "lastGreetedUser");

            request.setAttribute(
                    "lastUser",
                    lastUser);
        }

        request.getRequestDispatcher("/index.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String userName =
                request.getParameter("userName");

        HttpSession session = request.getSession();

        session.setAttribute("lastGreetedUser", userName);

        session.setMaxInactiveInterval(30 * 60);
        Cookie nameCookie =
                new Cookie(
                        "greetUserName",
                        userName);

        nameCookie.setMaxAge(
                7 * 24 * 60 * 60);

        nameCookie.setPath(
                "/GreetingWebAppCore");

        response.addCookie(
                nameCookie);

        Greeting greeting = greetingService.greet(userName);

        request.setAttribute(
                "greeting",
                greeting);

        request.getRequestDispatcher("/greeting.jsp")
                .forward(request, response);
    }

    @Override
    public void destroy() {

        if (context != null) {
            context.close();
        }

        System.out.println("=== GreetingServlet destroyed ===");
    }
}