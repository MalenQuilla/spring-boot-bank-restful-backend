package malenquillaa.java.spring.bank.services.security.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.logging.Logger;

public class JwtUtils {

    private static final Logger logger = Logger.getLogger(JwtUtils.class.getName());

    @Value("${malenquillaa.app.jwtSecret}")
    private String jwtSecret;

    @Value("${malenquillaa.app.jwtExpirationMs}")
    private long jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userDetails.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.warning("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            logger.warning("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            logger.warning("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            logger.warning("JWT claims string is empty");
        }
        return false;
    }
}
