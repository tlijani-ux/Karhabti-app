package com.karhabtiapp.configurations;

import com.karhabtiapp.services.jwt.UserService;
import com.karhabtiapp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Extract Authorization header
        final String authHeader = request.getHeader("Authorization");

        // Check if Authorization header is valid
        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract JWT token
        String jwt = authHeader.substring(7);

        // Extract user email from the token
        String userEmail = jwtUtil.extractUsername(jwt);

        // Check if user email is not empty and no authentication is present in the SecurityContext
        if (!StringUtils.isEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {

            System.out.println("Processing JWT for user: " + userEmail);

            // Load UserDetails from UserService
            UserDetails userDetails = userService.DetailsService().loadUserByUsername(userEmail);

            // Validate the token
            if (jwtUtil.validateToken(jwt, userDetails)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();

                // Create authentication token
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Set details from the request
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authenticationToken);

                // Set the SecurityContext with the authenticated token
                SecurityContextHolder.setContext(context);
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
