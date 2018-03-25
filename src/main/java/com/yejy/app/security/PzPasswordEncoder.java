package com.yejy.app.security;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自定义PasswordEncoder
 */
public class PzPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return charSequence.toString().equals(s);
    }
}
