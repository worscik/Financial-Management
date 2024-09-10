package pl.financemanagement.ApplicationConfig.JWToken;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret.key}")
    private final String secretKey;
    private static final Logger logger = LogManager.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // Pominięcie filtrów dla loginu i rejestracji
        if (requestURI.matches("^/h2-console.*")
                || "/users/register".equals(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt)) {
                if (validateToken(jwt)) {
                    String username = getUsernameFromJWT(jwt);
                    String role = getRoleFromJWT(jwt);

                    // Tworzenie listy ról (Authorities)
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

                    // Tworzenie obiektu Authentication z loginem i rolami
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, Collections.singletonList(authority));

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Ustawienie obiektu Authentication w SecurityContextHolder
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } else {
                    if (isTokenExpired(jwt)) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Token has expired");
                        return;
                    } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Invalid token");
                        return;
                    }
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Missing token");
                return;
            }

        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred while processing the token. Contact with admin!");
            response.getWriter().close();
            return;
        }

        filterChain.doFilter(request, response);
    }

    // Metoda do pobierania JWT z nagłówka
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // Walidacja tokenu JWT
    private boolean validateToken(String authToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(authToken);
            JWSVerifier verifier = new MACVerifier(secretKey);
            if (!signedJWT.verify(verifier)) {
                return false;
            }
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            return expirationTime != null && expirationTime.after(new Date());
        } catch (JOSEException | ParseException ex) {
            logger.error("Error during validate token: ", ex);
        }
        return false;
    }

    // Sprawdzenie, czy token wygasł
    private boolean isTokenExpired(String authToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(authToken);
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            return expirationTime != null && expirationTime.before(new Date());
        } catch (ParseException ex) {
            logger.error("Error during check token expired time ", ex);
        }
        return true;
    }

    // Pobieranie loginu (username) z JWT
    private String getUsernameFromJWT(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet().getSubject();
    }

    // Pobieranie roli z JWT
    private String getRoleFromJWT(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet().getStringClaim("role");
    }
}
