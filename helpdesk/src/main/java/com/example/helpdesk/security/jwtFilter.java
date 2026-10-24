package com.example.helpdesk.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class jwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public jwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. read the Authorization header
        String authHeader = request.getHeader("Authorization");

        // 2. if there's a Bearer token, process it
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);   // strip "Bearer "

            // 3. if the token is valid, authenticate the request
            if (jwtUtil.isTokenValid(token)) {
                String email = jwtUtil.extractEmail(token);

                // tell Spring Security this request is authenticated as this user
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 4. let the request continue
        filterChain.doFilter(request, response);
    }
}