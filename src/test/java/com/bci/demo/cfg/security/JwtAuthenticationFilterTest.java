package com.bci.demo.cfg.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @SuppressWarnings("unused")
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilterInternal_NoAuthorizationHeader_CallsFilterChain() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_InvalidToken_WritesBadRequest() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidtoken");
        doThrow(new RuntimeException("Malformed")).when(jwtUtil).extractUsername("invalidtoken");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response).setContentType("application/json");
        String json = sw.toString();
        assertTrue(json.contains("Token inválido o mal formado"));
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_UsernameNotFound_WritesUnauthorized() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer validtoken");
        when(jwtUtil.extractUsername("validtoken")).thenReturn("user");
        when(userDetailsService.loadUserByUsername("user")).thenThrow(new UsernameNotFoundException("not found"));

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        String json = sw.toString();
        assertTrue(json.contains("Usuario no encontrado"));
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_InvalidJwt_WritesUnauthorized() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer validtoken");
        when(jwtUtil.extractUsername("validtoken")).thenReturn("user");
        when(userDetailsService.loadUserByUsername("user")).thenReturn(userDetails);
        when(jwtUtil.validateToken("validtoken", userDetails)).thenReturn(false);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        String json = sw.toString();
        assertTrue(json.contains("Token JWT inválido o expirado"));
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_ValidToken_SetsAuthenticationAndCallsChain() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer validtoken");
        when(jwtUtil.extractUsername("validtoken")).thenReturn("user");
        when(userDetailsService.loadUserByUsername("user")).thenReturn(userDetails);
        when(jwtUtil.validateToken("validtoken", userDetails)).thenReturn(true);
        when(userDetails.getAuthorities()).thenReturn(java.util.Collections.emptyList());

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertTrue(auth instanceof UsernamePasswordAuthenticationToken);
        assertEquals(userDetails, auth.getPrincipal());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_ExceptionDuringUserDetails_WritesInternalServerError() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer validtoken");
        when(jwtUtil.extractUsername("validtoken")).thenReturn("user");
        when(userDetailsService.loadUserByUsername("user")).thenThrow(new RuntimeException("DB error"));

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(response).setContentType("application/json");
        String json = sw.toString();
        assertTrue(json.contains("Ocurrió un error inesperado"));
        verify(filterChain, never()).doFilter(request, response);
    }
}