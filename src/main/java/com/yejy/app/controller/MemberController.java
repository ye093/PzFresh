package com.yejy.app.controller;

import com.yejy.app.model.BaseModel;
import com.yejy.app.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberController {

    @Autowired
    MemberService memberService;

    @RequestMapping(value = "member", method = RequestMethod.GET)
    public ResponseEntity<BaseModel> userInfo(@RequestParam String mobile, @RequestParam String password) {
        System.out.println(mobile);
        System.out.println(password);
        return ResponseEntity.ok(new BaseModel(0, "ok", memberService.getMember(mobile, password)));
    }
}
