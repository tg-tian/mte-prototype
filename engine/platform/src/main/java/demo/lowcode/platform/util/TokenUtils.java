package demo.lowcode.platform.util;

import demo.lowcode.platform.entity.User;
import io.jsonwebtoken.*;

import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.security.Keys;
public class TokenUtils {

    //秘钥
    private static final Key SECURE_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String createToken(User user){
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, SECURE_KEY) //签发算法，密钥为jwtToken
                .claim("id",user.getUser_id()) //body数据
                .claim("username",user.getUser_name())
                .setIssuedAt(new Date()) //签发时间
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 *60 *60 * 1000)); //设置一天有效期
        String token = jwtBuilder.compact();
        return token;
    }

    public static Claims checkToken(String token){
        //从token中解析出claims
        try {
            Claims claims=Jwts.parser().setSigningKey(SECURE_KEY).parseClaimsJws(token).getBody();
            return claims;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
