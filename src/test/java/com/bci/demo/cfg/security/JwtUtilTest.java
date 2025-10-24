package com.bci.demo.cfg.security;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;

import io.jsonwebtoken.MalformedJwtException;

@SpringBootTest
@TestPropertySource(properties = {
    "jwt.secret=c2VjcmV0X2tleV9mb3JfandrX3Rlc3RzXzEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OTA=",
    "jwt.expiration=3600000"
})
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        userDetails = new User("test@example.com", "password", new ArrayList<>());
    }

    @Test
    public void testGenerateToken() {
        String token = jwtUtil.generateToken(userDetails);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    public void testExtractUsername() {
        String token = jwtUtil.generateToken(userDetails);
        String username = jwtUtil.extractUsername(token);
        assertEquals(userDetails.getUsername(), username);
    }

    @Test
    public void testValidateToken() {
        String token = jwtUtil.generateToken(userDetails);
        assertTrue(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    public void testValidateToken_invalidUsername() {
        String token = jwtUtil.generateToken(userDetails);
        UserDetails otherUserDetails = new User("other@example.com", "password", new ArrayList<>());
        assertFalse(jwtUtil.validateToken(token, otherUserDetails));
    }

    @Test
    public void testExtractExpiration() {
        String token = jwtUtil.generateToken(userDetails);
        Date expiration = jwtUtil.extractExpiration(token);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    public void testMalformedToken() {
        String malformedToken = "this.is.not.a.jwt";
        assertThrows(MalformedJwtException.class, () -> jwtUtil.extractUsername(malformedToken));
    }
}
