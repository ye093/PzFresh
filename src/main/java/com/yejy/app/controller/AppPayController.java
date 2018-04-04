package com.yejy.app.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.yejy.app.model.BaseModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/pay")
public class AppPayController {
    @Value("${alipay.server_url}")
    private String serverUrl;
    @Value("${alipay.app_id}")
    private String appId;
    @Value("${alipay.app_private_key}")
    private String appPriKey;
    @Value("${alipay.alipay_public_key}")
    private String alipayPubKey;
    @Value("${alipay.format}")
    private String format;
    @Value("${alipay.charset}")
    private String charset;
    @Value("${alipay.signType}")
    private String signType;

    @Value("${app.address}")
    private String serverAddress;


    @GetMapping(path = "/alipay")
    public ResponseEntity<BaseModel> alipay() {
        // 实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(serverUrl, appId, appPriKey, format, charset, alipayPubKey, signType);
        // 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        // SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("品珍商城-下单支付");
        model.setSubject("商城订单支付");
        model.setOutTradeNo("2574421445aa411");
        model.setTimeoutExpress("30m");
        model.setTotalAmount("0.01");
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(serverAddress + "/pay/alipay/result");
        String resBody = "";
        try {
            // 这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            resBody = response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(new BaseModel(0, "ok", resBody));
    }

    @PostMapping(path = "/alipay/result")
    public ResponseEntity<String> alipayResult(@RequestParam Map<String, String> params) {
        System.out.println(params);
        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        try {
            boolean flag = AlipaySignature.rsaCheckV1(params, alipayPubKey, charset, signType);
            if (flag) {
                return ResponseEntity.ok("SUCCESS");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("ERROR");
    }


}
