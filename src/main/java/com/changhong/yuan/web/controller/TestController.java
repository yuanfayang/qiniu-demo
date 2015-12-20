package com.changhong.yuan.web.controller;

import com.changhong.yuan.web.base.Utils.HMACSHA1Helper;
import com.changhong.yuan.web.qiniu.QiniuCloudConfig;
import com.google.gson.JsonObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.UrlSafeBase64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.changhong.yuan.web.qiniu.QiniuCloudConfig.bucket;

/**
 * Created with IntelliJ IDEA.
 *
 * @authr: Fayang Yuan
 * @Date: 2015/12/17
 * @Time: 22:12
 * @Description:
 */
@Controller
public class TestController {

    private UploadManager uploadManager;

    /**
     *
     * @param model
     * @return
     * 七牛上传策略
     * scope = 'my-bucket:sunflower.jpg'
    * deadline = 1451491200
    returnBody = '{
    "name": $(fname),
    "size": $(fsize),
    "w": $(imageInfo.width),
    "h": $(imageInfo.height),
    "hash": $(etag)
    }'
     */
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String test(Model model){
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
            sign = HMACSHA1Helper.HmacSHA1Encrypt(encodePutPolicy,QiniuCloudConfig.SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //对签名进行URL安全的Base64编码
        String encodeSign = UrlSafeBase64.encodeToString(sign);

        //将AccessKey、encodedSign和encodedPutPolicy用:连接起来：
        String uploadToken = QiniuCloudConfig.ACCESS_KEY+":"+encodeSign+":"+encodePutPolicy;

        model.addAttribute("token", uploadToken);
        return "test";
    }

  }
