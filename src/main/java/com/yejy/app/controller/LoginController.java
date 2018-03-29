package com.yejy.app.controller;

import com.yejy.app.data.LoginRecordMapper;
import com.yejy.app.model.BaseModel;
import com.yejy.app.model.LoginRecord;
import com.yejy.app.model.Member;
import com.yejy.app.service.MemberService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

    @Autowired
    LoginRecordMapper loginRecordMapper;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<BaseModel> login(@RequestParam(required = false) String mobile, @RequestParam(required = false) String password,
                                           @RequestParam(required = false) Integer source) {
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

        LoginRecord loginRecord = new LoginRecord();
        loginRecord.setMemberId(member.getMemberId());
        loginRecord.setType(source);
        loginRecord.setLoginTime(new Date(currentTimeMillis));
        loginRecord.setEnable("Y");
        LoginRecord existedRecord = loginRecordMapper.getLoginRecordByMemberId(member.getMemberId(), source);
        int result = 0;
        if (existedRecord != null) {
            loginRecord.setRecordId(existedRecord.getRecordId());
            result = loginRecordMapper.updateLoginRecord(loginRecord);
        } else {
            result = loginRecordMapper.addLoginRecord(loginRecord);
        }
        if (result == 1) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("member_id", member.getMemberId());
            map.put("card_no", member.getCardNo());
            map.put("nick", member.getNick());
            map.put("user_name", member.getUserName());
            map.put("mobile", member.getMobile());
            map.put("sex", member.getSex());
            map.put("score", member.getScore());
            MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
            header.add(headerKey, jwt);
            return new ResponseEntity(new BaseModel(0, "成功", map),header, HttpStatus.OK);
        }

        return ResponseEntity.ok(new BaseModel(-1, "系统出错", null));

    }
}
