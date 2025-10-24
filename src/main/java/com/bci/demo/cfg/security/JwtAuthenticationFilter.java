package com.bci.demo.cfg.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bci.demo.user.dto.ApiResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    private void writeErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");

        ApiResponseDTO<Object> apiResponse = new ApiResponseDTO<>(status, message);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(apiResponse);

        response.getWriter().write(json);
    }

    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception ex) {
                writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                        "Token inválido o mal formado: " + ex.getMessage());
                return;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (!jwtUtil.validateToken(jwt, userDetails)) {
                    writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                            "Token JWT inválido o expirado");
                    return;
                }

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

            } catch (UsernameNotFoundException ex) {
                writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "Usuario no encontrado: " + ex.getMessage());
                return;
            } catch (Exception ex) {
                writeErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Ocurrió un error inesperado: " + ex.getMessage());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}