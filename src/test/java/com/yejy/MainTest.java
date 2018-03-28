package com.yejy;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.junit.Assert;
import org.junit.Test;
import java.security.Key;

public class MainTest {


    @Test
    public void testA() {
        String key = "Y600516Lmm@_P`";
        String jwtString = Jwts.builder().setSubject("YeJY").signWith(SignatureAlgorithm.HS512, key).compact();
        System.out.println(jwtString);
        Assert.assertEquals("YeJY", Jwts.parser().setSigningKey(key).parseClaimsJws(jwtString).getBody()
        .getSubject());

    }
}
