package real.world.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import real.world.domain.user.entity.User;

@NoArgsConstructor
public final class JwtUtils {

    private static final String secretKey = "ThisIsA_SecretKeyForJwtExample";

    public static String generateJwtToken(User user) {
        JwtBuilder builder = Jwts.builder()
            .setHeader(createHeader())
            .setClaims(createClaims(user))
            .setExpiration(createExpireDateForOneYear())
            .signWith(SignatureAlgorithm.HS256, createSigningKey());

        return builder.compact();
    }

    public static String getJwtFromHeader(String header) {
        return header.split(" ")[1];
    }

    private static Date createExpireDateForOneYear() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 30);
        return c.getTime();
    }

    private static Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());

        return header;
    }

    private static Map<String, Object> createClaims(User user) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());

        return claims;
    }

    private static Key createSigningKey() {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    private static Claims getClaimsFormJwt(String jwt) {
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
            .parseClaimsJws(jwt).getBody();
    }

    private static String getUserEmailFromJwt(String jwt) {
        return (String) getClaimsFormJwt(jwt).get("email");
    }

    private static String getRoleFromJwt(String jwt) {
        return (String) getClaimsFormJwt(jwt).get("role");
    }

    public static boolean isValidJwt(String jwt) {
        try {
            Claims claims = getClaimsFormJwt(jwt);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static Authentication getAuthentication(String jwt) {
        final String userEmail = getUserEmailFromJwt(jwt);
        final String userRole = getRoleFromJwt(jwt);

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userRole));

        return new UsernamePasswordAuthenticationToken(userEmail, "", authorities);
    }
}