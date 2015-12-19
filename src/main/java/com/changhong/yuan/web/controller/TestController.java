package com.changhong.yuan.web.controller;

import com.changhong.yuan.web.qiniu.QiniuCloudConfig;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String test(Model model){
        String token = QiniuCloudConfig.auth.uploadToken(QiniuCloudConfig.bucket,QiniuCloudConfig.key);
        model.addAttribute("token",token);
        return "test";
    }

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public void uploud(@RequestParam(value = "file",required = true) MultipartFile file){
        System.out.println("======================================");
        String token = QiniuCloudConfig.auth.uploadToken(QiniuCloudConfig.bucket,QiniuCloudConfig.key);
        System.out.println("token:"+token);


        Response response = null;
        try {
            byte[] bytes = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            response = uploadManager.put("hello".getBytes(), QiniuCloudConfig.key,token);
        }
        catch (QiniuException e)
        {
            e.printStackTrace();
        }
    }
}
