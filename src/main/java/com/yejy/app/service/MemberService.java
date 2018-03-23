package com.yejy.app.service;

import com.yejy.app.model.Member;

public interface MemberService {
    Member getMember(String mobile, String password);
}
