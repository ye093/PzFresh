package com.yejy.app.data;

import com.yejy.app.model.LoginRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRecordMapper {

    LoginRecord getLoginRecordByMemberId(@Param("memberId") Integer memberId, @Param("type") Integer type);

    Integer addLoginRecord(LoginRecord loginRecord);

    Integer updateLoginRecord(LoginRecord loginRecord);

}
