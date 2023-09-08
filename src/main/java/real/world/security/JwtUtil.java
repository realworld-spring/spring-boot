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

    public String generateJwtToken(String id, String role) {
        return Jwts.builder()
            .claim("id", id)
            .claim("role", role) // 정보 저장
            .signWith(key, SignatureAlgorithm.HS512) // 사용할 암호화 알고리즘과 , signature 에 들어갈 secret값 세팅
            .setExpiration(expiredDate()) // set Expire Time 해당 옵션 안넣으면 expire안함
            .compact();
    }

    public boolean isValidJwt(String jwt) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
            return true;
        } catch (Exception e) { // todo Exception
            return false;
        }
    }

    public Authentication getAuthentication(String jwt) {
        final String id = getIdFromJwt(jwt);
        final String role = getRoleFromJwt(jwt);

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));

        return new JwtAuthenticationToken(id, authorities);
    }

    public String getJwtFromHeader(String header) {
        return header.split(" ")[1];
    }

    private Date expiredDate() {
        LocalDateTime expiredDate = LocalDateTime.now().plusDays(30);
        return Timestamp.valueOf(expiredDate);
    }

    private Claims getClaimsFromJwt(String jwt) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
    }

    private String getIdFromJwt(String jwt) {
        return (String) getClaimsFromJwt(jwt).get("id");
    }

    private String getRoleFromJwt(String jwt) {
        return (String) getClaimsFromJwt(jwt).get("role");
    }

}