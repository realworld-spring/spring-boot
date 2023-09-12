package real.world.security;

import static real.world.error.ErrorCode.JWT_EXPIRED;
import static real.world.error.ErrorCode.JWT_FORMAT_INVALID;
import static real.world.error.ErrorCode.JWT_SIGNATURE_INVALID;
import static real.world.error.ErrorCode.JWT_CLAIMS_EMPTY;
import static real.world.error.ErrorCode.JWT_UNSUPPORTED;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import real.world.error.exception.AuthenticationErrorCodeException;
import real.world.security.jwt.JwtAuthenticationToken;

@Component
public class JwtUtil {

    private final Key key;

    public JwtUtil(@Value("${auth.key}") byte[] key) {
        this.key = Keys.hmacShaKeyFor(key);
    }

    public String generateJwtToken(String principal, String role) {
        return Jwts.builder()
            .claim("principal", principal)
            .claim("role", role)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(expiredDate())
            .compact();
    }

    public Authentication getAuthentication(String jwt) {
        verify(jwt);

        final String id = getPrincipalFromJwt(jwt);
        final String role = getRoleFromJwt(jwt);

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));

        return new JwtAuthenticationToken(id, authorities);
    }

    private void verify(String jwt) throws AuthenticationErrorCodeException {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
        }  catch (SecurityException | MalformedJwtException e) {
            throw new AuthenticationErrorCodeException(JWT_SIGNATURE_INVALID);
        } catch (ExpiredJwtException e) {
            throw new AuthenticationErrorCodeException(JWT_EXPIRED);
        } catch (UnsupportedJwtException e) {
            throw new AuthenticationErrorCodeException(JWT_UNSUPPORTED);
        } catch (IllegalArgumentException e) {
            throw new AuthenticationErrorCodeException(JWT_CLAIMS_EMPTY);
        } catch (JwtException e) {
            throw new AuthenticationErrorCodeException(JWT_FORMAT_INVALID);
        }
    }

    private Date expiredDate() {
        final LocalDateTime expiredDate = LocalDateTime.now().plusDays(30);
        return Timestamp.valueOf(expiredDate);
    }

    private Claims getClaimsFromJwt(String jwt) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
    }

    private String getPrincipalFromJwt(String jwt) {
        return (String) getClaimsFromJwt(jwt).get("principal");
    }

    private String getRoleFromJwt(String jwt) {
        return (String) getClaimsFromJwt(jwt).get("role");
    }

}