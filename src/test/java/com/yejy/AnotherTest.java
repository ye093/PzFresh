package com.yejy;

import net.sf.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class AnotherTest {

    @Test
    public void test() throws Exception{
        JSONObject object = new JSONObject();
        object.put("code", 1);
        object.put("msg", "w2");
        Map<String, Object> data = new HashMap<>();
        data.put("key", "32jlljj");
        data.put("aaa", "dss");
        object.put("data", data);
        System.out.println(object.toString());
    }
}
