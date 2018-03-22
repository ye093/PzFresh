package com.yejy;

import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Locale;

public class AnotherTest {

    @Test
    public void test() throws Exception{
        String a = decryption("UdtpIgz4xm8=", "ioqhx2KMPUY=");
        System.out.println(a);
    }

    /**
     * DES解密
     * @param secretData 密码字符串
     * @param secretKey 解密密钥
     * @return 原始字符串
     * @throws Exception
     */
    public static String decryption(String secretData, String secretKey) throws Exception {

        Cipher cipher = null;
        try {
            //
            cipher = Cipher.getInstance("DES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, generateKey(secretKey));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new Exception("NoSuchAlgorithmException", e);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            throw new Exception("NoSuchPaddingException", e);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new Exception("InvalidKeyException", e);

        }

        try {

            byte[] buf = cipher.doFinal(hexStr2Bytes(secretData));

            return new String(buf,"utf-8");

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("IllegalBlockSizeException", e);
        }
    }
    public static byte[] hexStr2Bytes(String src){
        /*对输入值进行规范化整理*/
        src = src.trim().replace(" ", "").toUpperCase(Locale.US);
        //处理值初始化
        int m=0,n=0;
        int iLen=src.length()/2; //计算长度
        byte[] ret = new byte[iLen]; //分配存储空间

        for (int i = 0; i < iLen; i++){
            m=i*2+1;
            n=m+1;
            ret[i] = (byte)(Integer.decode("0x"+ src.substring(i*2, m) + src.substring(m,n)) & 0xFF);
        }
        return ret;
    }

    /**
     * 获得秘密密钥
     *
     * @param secretKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     */
    private static SecretKey generateKey(String secretKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        DESKeySpec keySpec = new DESKeySpec(secretKey.getBytes());
        keyFactory.generateSecret(keySpec);
        return keyFactory.generateSecret(keySpec);
    }
}
