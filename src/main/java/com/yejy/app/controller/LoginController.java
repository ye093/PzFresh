package com.yejy.app.controller;

import com.yejy.app.model.BaseModel;
import com.yejy.app.model.Member;
import com.yejy.app.service.MemberService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    MemberService memberService;

    @Value("${token.key}")
    private String tokenKey;

    @Value("${token.issuer}")
    private String tokenIssuer;

    @Value("${token.subject}")
    private String tokenSubject;

    @Value("${token.expiration}")
    private Long tokenExpiration;

    @Value("${token.header}")
    private String headerKey;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<BaseModel> login(@RequestParam String mobile, @RequestParam String password,
                                           @RequestParam Integer source) {
        if (mobile == null || password == null) {
            return ResponseEntity.ok(new BaseModel(-1, "参数错误", null));
        }
        Member member = memberService.getMember(mobile, password);
        if (member == null) {
            return ResponseEntity.ok(new BaseModel(-1, "账号或密码错误", null));
        }
        Long currentTimeMillis = System.currentTimeMillis();
        // 生成token
        Map<String, Object> data = new HashMap<>();
        data.put("member_id", member.getMemberId());
        data.put("mobile", member.getMobile());
        data.put("source", source);
        data.put("login_time", currentTimeMillis);
        Date date = new Date(currentTimeMillis + tokenExpiration);
        String jwt =
                Jwts.builder().setIssuer(tokenIssuer)
                        .setSubject(tokenSubject)
                        .setExpiration(date)
                        .addClaims(data)
                        .signWith(SignatureAlgorithm.HS256, tokenKey)
                        .compact();
        ResponseEntity responseEntity = new ResponseEntity(new BaseModel(0, "成功", member),null, HttpStatus.OK);

        return responseEntity;

    }
}
