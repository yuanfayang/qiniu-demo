package com.changhong.yuan.web.qiniu;

import com.changhong.yuan.web.base.Utils.HMACSHA1Helper;
import com.google.gson.JsonObject;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;

import static com.changhong.yuan.web.qiniu.QiniuCloudConfig.auth;
import static com.changhong.yuan.web.qiniu.QiniuCloudConfig.bucket;

/**
 * Created with IntelliJ IDEA.
 *
 * @authr: Fayang Yuan
 * @Date: 2015/12/28
 * @Time: 19:44
 * @Description: 生成上传token
 */
public class UploadTokenHelper {

    /**
     * 生成前端上传的token
     * @return
     */
    public static String buildFrontEndUploadToken() {
        String token = QiniuCloudConfig.auth.uploadToken(bucket);

        //七牛上传策略
        JsonObject uploadStrategyJsonObject = new JsonObject();
        uploadStrategyJsonObject.addProperty("scope", QiniuCloudConfig.bucket);
        uploadStrategyJsonObject.addProperty("deadline", 1451491200);
        uploadStrategyJsonObject.addProperty("returnBody", "{'" +
                "name':$(fname)" +
                "'size':$(fsize)" +
                "'w':$(imageInfo.width)" +
                "'h':$(imageInfo.height)" +
                "'hash':$(etag)" +
                "}");

        //对JSON编码的上传策略进行URL安全的Base64编码
        String encodePutPolicy = UrlSafeBase64.encodeToString(uploadStrategyJsonObject.toString());

        //使用SecretKey对上一步生成的待签名字符串计算HMAC-SHA1签名
        byte[] sign = null;
        try {
            sign = HMACSHA1Helper.HmacSHA1Encrypt(encodePutPolicy, QiniuCloudConfig.SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //对签名进行URL安全的Base64编码
        String encodeSign = UrlSafeBase64.encodeToString(sign);

        //将AccessKey、encodedSign和encodedPutPolicy用:连接起来：
        String uploadToken = QiniuCloudConfig.ACCESS_KEY + ":" + encodeSign + ":" + encodePutPolicy;

        return uploadToken;
    }

    /**
     * 生成后端上传的token
     * @return
     */
    public static String buildBackEndToken(){
        return auth.uploadToken(QiniuCloudConfig.bucket,null,3600,new StringMap().putNotEmpty("returnBody","{\"key\": $(key), \"hash\": $(etag), \"width\": $(imageInfo.width), \"height\": $(imageInfo.height)}"));
    }
}
