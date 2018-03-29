package com.yejy;


import com.yejy.app.Application;
import com.yejy.app.data.LoginRecordMapper;
import com.yejy.app.model.LoginRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class SpringTestAp {
    @Autowired
    LoginRecordMapper loginRecordMapper;

    @Test
    public void contextLoads() {





        LoginRecord loginRecord = new LoginRecord();
        loginRecord.setDeviceName("苹果X");
        loginRecord.setEnable("Y");
        loginRecord.setLoginTime(new Date(System.currentTimeMillis()));
        loginRecord.setType(1);
        loginRecord.setMemberId(74);

        LoginRecord existedRecord = loginRecordMapper.getLoginRecordByMemberId(73, 1);
        if (existedRecord != null) {
            loginRecord.setRecordId(existedRecord.getRecordId());
            System.out.println("更新: " + loginRecordMapper.updateLoginRecord(loginRecord));
        } else {
            System.out.println("新增：" + loginRecordMapper.addLoginRecord(loginRecord));
        }
    }

    @Test
    public void myTest() {
        LoginRecord loginRecord = loginRecordMapper.getLoginRecordByMemberId(72, 1);
        System.out.println(loginRecord);
    }
}
