package real.world.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import lombok.NoArgsConstructor;
import real.world.domain.user.entity.User;

@NoArgsConstructor
public final class TokenUtils {

    private static final String secretKey = "ThisIsA_SecretKeyForJwtExample";

    public static String generateJwtToken(User user) {
        JwtBuilder builder = Jwts.builder()
//            .setSubject(user.getId().toString())
//            .setHeader(createHeader())
            .setClaims(createClaims(user))
            .setExpiration(createExpireDateForOneYear())
            .signWith(SignatureAlgorithm.HS256, createSigningKey());

        return builder.compact();
    }


    public static String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }

    private static Date createExpireDateForOneYear() {
        // 토큰 만료시간은 30일으로 설정
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
        // 공개 클레임에 사용자의 이름과 이메일을 설정하여 정보를 조회할 수 있다.
        Map<String, Object> claims = new HashMap<>();

        claims.put("id", user.getId());
        claims.put("role", user.getRole());

        return claims;
    }

    private static Key createSigningKey() {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
//        byte[] apiKeySecretBytes = Key.hmacShaKeyFor(accessSecret);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    private static Claims getClaimsFormToken(String token) {
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
            .parseClaimsJws(token).getBody();
    }

    private static String getUserEmailFromToken(String token) {
        Claims claims = getClaimsFormToken(token);
        return (String) claims.get("email");
    }

    private static UserRole getRoleFromToken(String token) {
        Claims claims = getClaimsFormToken(token);
        return (UserRole) claims.get("role");
    }

    public static boolean isValidToken(String token) {
        try {
            Claims claims = getClaimsFormToken(token);
            return true;

        } catch (ExpiredJwtException exception) {
            return false;
        }
    }
}