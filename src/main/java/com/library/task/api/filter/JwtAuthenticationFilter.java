    package com.library.task.api.filter;


    import com.fasterxml.jackson.databind.ObjectMapper;
    import com.library.task.shared.dtos.errors.ApiErrorResponse;
    import com.library.task.infrastructure.security.CustomUserDetailsService;
    import com.library.task.infrastructure.security.jwt.JwtUtil;
    import io.jsonwebtoken.ExpiredJwtException;
    import io.jsonwebtoken.MalformedJwtException;
    import io.jsonwebtoken.UnsupportedJwtException;
    import io.jsonwebtoken.security.SignatureException;
    import jakarta.servlet.FilterChain;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.HttpStatus;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
    import org.springframework.stereotype.Component;
    import org.springframework.web.filter.OncePerRequestFilter;

    import java.io.IOException;
    import java.time.LocalDateTime;

    @Component
    public class JwtAuthenticationFilter extends OncePerRequestFilter {

        private final JwtUtil jwtUtil;

        private CustomUserDetailsService userDetailsService;

        @Autowired
        public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
            this.jwtUtil = jwtUtil;
            this.userDetailsService = userDetailsService;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException{
            try {
                final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

                Long userId = null;
                String jwt = null;

                if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                    jwt = authorizationHeader.substring(7);
                    userId = jwtUtil.extractUserId(jwt);
                }

                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserById(userId);
                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
               chain.doFilter(request, response);
            }catch (ExpiredJwtException ex) {
                handleException(response, HttpStatus.UNAUTHORIZED, "JWT token has expired. Please log in again.");
            } catch (UnsupportedJwtException | MalformedJwtException | SignatureException ex) {
                handleException(response, HttpStatus.BAD_REQUEST, "Invalid JWT token. Please provide a valid token.");
            } catch (IllegalArgumentException ex) {
                handleException(response, HttpStatus.BAD_REQUEST, "JWT token is invalid or empty. Please provide a valid token.");
            } catch (RuntimeException ex) {
                handleException(response, HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred: " + ex.getMessage());
            }
        }

        private void handleException(HttpServletResponse response, HttpStatus status, String message) throws IOException {
            ApiErrorResponse errorResponse = new ApiErrorResponse(LocalDateTime.now(), message, status.value());
            response.setStatus(status.value());
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().findAndRegisterModules() .writeValueAsString(errorResponse));
        }

    }
