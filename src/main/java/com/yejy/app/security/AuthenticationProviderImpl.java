package com.yejy.app.security;

import com.yejy.app.model.Member;
import com.yejy.app.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationProviderImpl implements AuthenticationProvider {
    @Autowired
    MemberService memberService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String mobile = authentication.getName(); // 手机号
        String password = (String) authentication.getCredentials();
        if (password == null || password.isEmpty()) {
            throw new BadCredentialsException("密码不正确");
        }
        Member member = memberService.getMember(mobile, null);
        if (member == null || !password.equals(member.getPassword())) {
            throw new BadCredentialsException("密码不正确");
        }
        return new UsernamePasswordAuthenticationToken(mobile, password, null);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
