package com.yejy;

import org.junit.Assert;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

public class MainTest {


    @Test
    public void testA() {
        String key = initKey();
        String data = "99999900";
        byte[] dataBuf = data.getBytes();
//        int a = dataBuf.length % 8;
//        if (a != 0) a = 8 - a;
//        byte[] newDataBuf = new byte[dataBuf.length + a];
//        for (int i = 0; i < newDataBuf.length; i++) {
//            if (i < dataBuf.length )
//                newDataBuf[i] = dataBuf[i];
//             else
//                newDataBuf[i] = 0;
//        }
        byte[] encBuf = encrypt(dataBuf, key);
        String appData = Base64.getEncoder().encodeToString(encBuf);
        System.out.println("key: " + key + ", appData:" + appData);

        //后台代码
        byte[] re = Base64.getDecoder().decode(appData);
        byte[] decBuf = decrypt(re, key);
        String data2 = new String(decBuf).trim();
        System.out.println(data2);
        Assert.assertEquals(data, data2);
    }

    /**
     * 生成密钥
     */
    private String initKey() {
        try {
            //KeyGenerator 密钥生成器
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
            //初始化密钥生成器
            keyGenerator.init(56);
            //生成密钥
            SecretKey secretKey = keyGenerator.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            System.out.println("initKey: " + e.getMessage());
        }
        return null;
    }

    private byte[] encrypt(byte[] data, String keyStr) {
        byte[] key = Base64.getDecoder().decode(keyStr);
        //恢复密钥
        SecretKey secretKey = new SecretKeySpec(key, "DES");
        try {
            //Cipher完成加密或解密工作
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            //根据密钥对Cipher进行初始化 ENCRYPT_MODE, DECRYPT_MODE
//            cipher.init(Cipher.ENCRYPT_MODE, secretKey,new IvParameterSpec(key));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            //加密
            return cipher.doFinal(data);
        } catch (Exception e) {
            System.out.println("encrypt: " + e.getMessage());
        }
        return null;
    }

    public byte[] decrypt(byte[] data, String keyStr) {
        byte[] key = Base64.getDecoder().decode(keyStr);
        SecretKey secretKey = new SecretKeySpec(key, "DES");
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
//            cipher.init(Cipher.DECRYPT_MODE, secretKey,new IvParameterSpec(key));
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            System.out.println("decrypt: " + e.getMessage());
        }
        return null;
    }


    @Test
    public void myTest() throws Exception {
        String key = "A1B2C3D4E5F60708";
        String data = "amigoxie";
        String aa = encrypt1(data, key);

        //后台代码
        String bb = decrypt1(aa, key);
        Assert.assertEquals(data, bb);
    }

    //算法名称
    public static final String KEY_ALGORITHM = "DES";
    //算法名称/加密模式/填充方式
    //DES共有四种工作模式-->>ECB：电子密码本模式、CBC：加密分组链接模式、CFB：加密反馈模式、OFB：输出反馈模式
    public static final String CIPHER_ALGORITHM = "DES/ECB/NoPadding";

    private static int parse(char c) {
        if (c >= 'a') return (c - 'a' + 10) & 0x0f;
        if (c >= 'A') return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }

    private static SecretKey keyGenerator(String keyStr) throws Exception {
        byte input[] = HexString2Bytes(keyStr);
        DESKeySpec desKey = new DESKeySpec(input);
        //创建一个密匙工厂，然后用它把DESKeySpec转换成
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        return securekey;
    }

    public static byte[] HexString2Bytes(String hexstr) {
        byte[] b = new byte[hexstr.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = hexstr.charAt(j++);
            char c1 = hexstr.charAt(j++);
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));
        }
        return b;
    }

    public static String encrypt1(String data, String key) throws Exception {
        Key deskey = keyGenerator(key);        // 实例化Cipher对象，它用于完成实际的加密操作
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        SecureRandom random = new SecureRandom();        // 初始化Cipher对象，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, deskey, random);


        byte[] results = cipher.doFinal(data.getBytes());        // 该部分是为了与加解密在线测试网站（http://tripledes.online-domain-tools.com/）的十六进制结果进行核对
        for (int i = 0; i < results.length; i++) {
            System.out.print(results[i] + " ");
        }
        System.out.println();        // 执行加密操作。加密后的结果通常都会用Base64编码进行传输
        return Base64.getEncoder().encodeToString(results);
    }

    public static String decrypt1(String data, String key) throws Exception {
        Key deskey = keyGenerator(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        //初始化Cipher对象，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, deskey);        // 执行解密操作
        return new String(cipher.doFinal(Base64.getDecoder().decode(data)));
    }

    @Test
    public void yeTest() {
        BigDecimal a = BigDecimal.TEN;
        System.out.println(a.toPlainString());
    }
}
