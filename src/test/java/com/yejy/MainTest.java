package com.yejy;


import io.jsonwebtoken.*;
import org.junit.Test;
import java.util.*;

public class MainTest {
    String key = "Y600516Lmm@_P`";

    @Test
    public void testA() {

//        String jwtString = Jwts.builder().setSubject("YeJY").signWith(SignatureAlgorithm.HS512, key).compact();
//        System.out.println(jwtString);
//        Assert.assertEquals("YeJY", Jwts.parser().setSigningKey(key).parseClaimsJws(jwtString).getBody()
//        .getSubject());

        Map<String, Object> data = new HashMap<>();
        data.put("member_id", 72);
        Date date = new Date(System.currentTimeMillis() + 5 * 60 * 1000L);
        String jwt =
                Jwts.builder().setIssuer("http://www.yejinyun.cn/")
                        .setSubject("13630159257")
                        .setExpiration(date)
                        .addClaims(data)
                        .signWith(SignatureAlgorithm.HS256, key)
                        .compact();
        System.out.println(jwt);
    }

    @Test
    public void parseToken() {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(key)
                    .requireIssuer("http://www.yejinyun.cn/")
                    .requireSubject("13630159257")
                    .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vd3d3LnllamlueXVuLmNuLyIsInN1YiI6IjEzNjMwMTU5MjU3IiwiZXhwIjoxNTIyMzA4OTU0LCJtZW1iZXJfaWQiOjcyfQ.JmvWAZUe_lcKYUNAWb7K1NV-pJBie9_ZsoeVj_7xDI4");
            Map<String, Object> data = claims.getBody();
            Object memberId = data.get("member_id");
            System.out.println("memberId:" + memberId);


            //OK, we can trust this JWT

        } catch (ExpiredJwtException e) {
            System.out.println("已经过期");
        } catch (SignatureException e) {
            System.out.println(e.getMessage());
            //don't trust the JWT!
        } catch (Exception e) {
            System.out.println("token error");
        }
    }




}
