package com.agitation.sportseller.utils;

import android.content.Context;
import android.util.Log;

import com.alibaba.wireless.security.jaq.JAQException;
import com.alibaba.wireless.security.jaq.SecurityCipher;
import com.alibaba.wireless.security.jaq.SecuritySignature;
import com.alibaba.wireless.security.jaq.SecurityStorage;

/**
 * 加密 解密相关的工具类
 * Created by Fanxl on 2016/1/16.
 */
public class SecurityUtils {

    private static final String KEY = "9983fa3c-dfcd-435f-a63c-18db5e17eae3";

    /**
     * 加密字符串
     * @param sourceStr 待加密的数据
     * @param context
     * @return
     */
    public static String encryptionStr(String sourceStr, Context context){
        //安全加解密
        SecurityCipher securityCipher = new SecurityCipher(context);
        try {
            //"ka1":加密用的密钥key
            String encryptString = securityCipher.encryptString(sourceStr, "ka1");
            return  encryptString;
        } catch (JAQException e) {
            Log.e("encryption", "errorCode =" + e.getErrorCode());
        }
        return null;
    }

    /**
     * 加密数组
     * @param sourceByte 待加密数组
     * @param context
     * @return
     */
    public static byte[] encryptionByte(byte[] sourceByte, Context context){
        //安全加解密
        SecurityCipher securityCipher = new SecurityCipher(context);
        try {
            //"ka1"		 :加密用的密钥key
            byte[] encryptionBytes = securityCipher.encryptBinary(sourceByte, "ka1");
            return encryptionBytes;
        } catch (JAQException e) {
            Log.e("encryption", "errorCode =" + e.getErrorCode());
        }
        return null;
    }

    /**
     * 解密字符串
     * @param sourceStr 待解密字串
     * @param context
     * @return
     */
    public static String decryptStr(String sourceStr, Context context){
        //安全加解密
        SecurityCipher securityCipher = new SecurityCipher(context);
        try {
            String decryptString = securityCipher.decryptString(sourceStr, "ka1");
            return decryptString;
        } catch (JAQException e) {
            Log.e("decrypt", "errorCode =" + e.getErrorCode());
        }
        return null;
    }

    /**
     * 解密数组
     * @param sourceByte 待解密数组
     * @param context
     * @return
     */
    public static byte[] decryptByte(byte[] sourceByte, Context context){
        //安全加解密
        SecurityCipher securityCipher = new SecurityCipher(context);
        try {
            //"ka1":解密用的密钥key
            byte[] decryptBytes = securityCipher.decryptBinary(sourceByte, "ka1");
            return decryptBytes;
        } catch (JAQException e) {
            Log.e("decrypt", "errorCode =" + e.getErrorCode());
        }
        return null;
    }

    /**
     * 安全签名
     * @param sourceStr 待签名的字串
     * @param context 上下文
     * @return
     */
    public static String signStr(String sourceStr, Context context){
        SecuritySignature securitySignature = new SecuritySignature(context);
        try {
            //签名使用的指定密钥key
            String signStr = securitySignature.sign(sourceStr, "ka1");
            //将签名结果和原始数据一起发送到服务端，服务端根据原始数据重新计算签名，并与发送的签名进行比对，进而完成数据校验。
            return signStr;
        } catch (JAQException e) {
            Log.e("signStr", "errorCode =" + e.getErrorCode());
        }
        return null;
    }

    /**
     * 安全保存
     * @param sourceStr 待安全存储的字符串
     * @param context
     * @return
     */
    public static int securitySave(String key, String sourceStr, Context context){
        int result = 0;
        SecurityStorage securityStorage = new SecurityStorage(context);
        try {
            //保存"helloword"
            result = securityStorage.putString(key, sourceStr);

            //获取"helloword"
            securityStorage.getString("mykey");

            //删除"helloword"
            securityStorage.removeString("mykey");

        } catch (JAQException e) {
            Log.e("securitySave", "errorCode =" + e.getErrorCode());
        }
        return result;
    }

    /**
     * 获取安全存储的数据
     * @param key 获取数据的key
     * @param context
     * @return
     */
    public static String securityGet(String key, Context context){
        SecurityStorage securityStorage = new SecurityStorage(context);
        try {
            String result = securityStorage.getString(key);
            return result;
        } catch (JAQException e) {
            Log.e("securityGet", "errorCode =" + e.getErrorCode());
        }
        return null;
    }

    /**
     * 删除安全存储的数据
     * @param key 删除数据的key
     * @param context
     */
    public static void securityDelete(String key, Context context){
        SecurityStorage securityStorage = new SecurityStorage(context);
        try {
            securityStorage.removeString(key);
        } catch (JAQException e) {
            Log.e("securityDelete", "errorCode =" + e.getErrorCode());
        }
    }

}
