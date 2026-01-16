package com.example.userauthentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtility jwtUtility;
    private final UserDetailsService userDetailsService;
    @Autowired
    public JwtFilter(JwtUtility jwtUtility, UserDetailsService userDetailsService) {
        this.jwtUtility = jwtUtility;
        this.userDetailsService = userDetailsService;
    }
   // if token stored in header
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain chain) throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            String userName = jwtUtility.extractUserName(token);
//
//            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
//            UsernamePasswordAuthenticationToken auth =  new UsernamePasswordAuthenticationToken(
//                    userDetails, null, userDetails.getAuthorities()
//            );
//            SecurityContextHolder.getContext().setAuthentication(auth);
//        }
//
//        chain.doFilter(request, response);
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                HttpServletResponse response,
                                   FilterChain chain) throws ServletException, IOException {
        String token = null;

        if (request.getCookies() != null) {
            for (Cookie cookie: request.getCookies()) {
                if ("JWT".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null) {
            String userName = jwtUtility.extractUserName(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        chain.doFilter(request, response);
    }


}
