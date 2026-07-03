package com.greet.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class LoggingFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq =
                (HttpServletRequest) request;

        long startTime =
                System.currentTimeMillis();

        String method =
                httpReq.getMethod();

        String uri =
                httpReq.getRequestURI();

        System.out.println(
                ">>> REQUEST: "
                        + method
                        + " "
                        + uri);

        chain.doFilter(request, response);

        long duration =
                System.currentTimeMillis()
                        - startTime;

        System.out.println(
                "<<< RESPONSE: "
                        + method
                        + " "
                        + uri
                        + " completed in "
                        + duration
                        + " ms");
    }
}