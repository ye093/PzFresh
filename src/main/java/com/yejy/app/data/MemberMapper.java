package com.yejy.app.data;

import com.yejy.app.model.Member;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberMapper {
    Member getMember(@Param("mobile") String mobile, @Param("password") String password);
}
