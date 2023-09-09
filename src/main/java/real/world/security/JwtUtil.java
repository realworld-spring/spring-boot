package real.world.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

    private void verify(String jwt) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
        } catch (Exception e) { // todo Exception
            throw e;
        }
    }

    private Date expiredDate() {
        LocalDateTime expiredDate = LocalDateTime.now().plusDays(30);
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