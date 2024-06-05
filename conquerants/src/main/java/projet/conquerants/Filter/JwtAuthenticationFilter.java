package projet.conquerants.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import projet.conquerants.Service.JwtService;
import projet.conquerants.Service.UserDetailsServiceImpl;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private UserDetailsServiceImpl userDetailsImp;
    private final String COOKIE_NAME = "token";
    private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsServiceImpl userDetailsImp) {
        this.jwtService = jwtService;
        this.userDetailsImp = userDetailsImp;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            if (request.getCookies() != null) {
                String token = null;

                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals(COOKIE_NAME)) {
                        token = cookie.getValue();
                    }
                }

                if (token == null) {
                    filterChain.doFilter(request, response);
                    return;
                }

                String username = jwtService.extractUsername(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsImp.loadUserByUsername(username);

                    if (jwtService.isValid(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }

                }
            }

            filterChain.doFilter(request, response);
            logger.info("Token is valid");
            
        } catch (Exception e) {
            logger.warn("Token is invalid");

            Cookie cookie = new Cookie(COOKIE_NAME, null);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            response.setHeader("Set-Cookie", String.format(
                    "%s=%s; Expires=%s; Max-Age=%d; Path=/; HttpOnly; Secure; SameSite=None",
                    cookie.getName(),
                    cookie.getValue(),
                    Integer.toString(0),
                    cookie.getMaxAge()
            ));
        }
    }
}
