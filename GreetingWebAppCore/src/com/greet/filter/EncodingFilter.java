package com.greet.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class EncodingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig)
            throws ServletException {

        System.out.println(
                "=== EncodingFilter initialized ===");
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        request.setCharacterEncoding("UTF-8");

        response.setCharacterEncoding("UTF-8");

        response.setContentType(
                "text/html; charset=UTF-8");

        System.out.println(
                "EncodingFilter → request URI: "
                        + ((HttpServletRequest) request)
                        .getRequestURI());

        chain.doFilter(request, response);

        System.out.println(
                "EncodingFilter → response complete");
    }

    @Override
    public void destroy() {

        System.out.println(
                "=== EncodingFilter destroyed ===");
    }
}