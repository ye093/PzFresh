package com.yejy.app.service.impl;

import com.yejy.app.data.MemberMapper;
import com.yejy.app.model.Member;
import com.yejy.app.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberMapper memberMapper;

    @Override
    public Member getMember(String mobile, String password) {
        return memberMapper.getMember(mobile, password);
    }
}
