package dev.nitin.server.security;

import dev.nitin.server.exception.UnauthorizedException;
import dev.nitin.server.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            String header = request.getHeader("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                throw new UnauthorizedException("Missing or invalid Authorization header");
            }

            String token = header.substring(7);
//            System.out.println(">>> Extracted JWT: " + token);
//            System.out.println(">>> Validating JWT...");

            if (!jwtService.validateToken(token)) {
//                System.out.println(">>> JWT validation failed.");
                throw new UnauthorizedException("Invalid or expired JWT");
            }
//            System.out.println(">>> JWT validation passed.");

            String userId = jwtService.extractUserId(token);
            String userName = jwtService.extractUserName(token);
//            System.out.println(">>> JWT validated for userId: " + userId + ", name: " + name);
            if (userName == null || userName.isBlank() || userId == null || userId.isBlank()) {
                throw new UnauthorizedException("Invalid JWT payload");
            }

            CustomUserPrincipal principal = new CustomUserPrincipal(userId, userName);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(principal, null, List.of());
            SecurityContextHolder.getContext().setAuthentication(auth);

            chain.doFilter(request, response);

        } catch (UnauthorizedException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"" + ex.getMessage() + "\"}");
        }
    }
}