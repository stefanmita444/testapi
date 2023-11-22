package com.example.testapi.config;

import com.example.testapi.services.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter{

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException, UsernameNotFoundException {

        String path = request.getRequestURI();
        log.info(path);
        if (path.startsWith("/register") || path.startsWith("/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String principal;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Extract the token without the Bearer String
            jwt = authHeader.substring(7);
            // Extract the username from token
            principal = jwtTokenUtil.getUsernameFromToken(jwt);
            log.info("The principal inside jwtFilter is " + principal);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Error inside JwtRequestFilter class server error, JWT might be corrupted");
            return;
        }

        if (principal != null && SecurityContextHolder.getContext().getAuthentication() == null && !principal.isEmpty()) {
            UserDetails userDetails;
            try {
                userDetails = this.jwtUserDetailsService.loadUserByUsername(principal);
            } catch (UsernameNotFoundException e) {
                log.info("Error inside JwtRequestFilter, JWT might be corrupt. Please log in again or register to get a new JWT");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set appropriate HTTP status
                response.getWriter().write("User not found.");  // Inform the client about the error
                return;  // Exit the method
            }


            if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(),
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // Once we get the token validate it.
        filterChain.doFilter(request, response);
    }
}
