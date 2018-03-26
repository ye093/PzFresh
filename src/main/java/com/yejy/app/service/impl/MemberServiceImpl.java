package com.yejy.app.service.impl;

import com.yejy.app.data.MemberMapper;
import com.yejy.app.model.Member;
import com.yejy.app.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService, UserDetailsService {
    @Autowired
    MemberMapper memberMapper;

    @Override
    public Member getMember(String mobile, String password) {
        return memberMapper.getMember(mobile, password);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // 这里的s指的是手机号
        return memberMapper.getMember(s, null);
    }
}
