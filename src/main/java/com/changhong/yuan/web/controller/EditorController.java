package com.changhong.yuan.web.controller;

import com.changhong.yuan.web.qiniu.UploadTokenHelper;
import com.google.gson.JsonObject;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @authr: Fayang Yuan
 * @Date: 2015/12/28
 * @Time: 22:25
 * @Email: flyyuanfayang@sina.com
 * @Description: 富文本编辑器Controller
 */
@Controller
public class EditorController {
    private final Log log = LogFactory.getLog(EditorController.class);

    @RequestMapping(value = "/editor", method = RequestMethod.GET)
    public String view(Model model) {
        model.addAttribute("token", UploadTokenHelper.buildUploadToken());
        return "editor";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(HttpServletRequest request,
                       HttpServletResponse response) {
        JsonObject jsonObject = new JsonObject();
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (ServletFileUpload.isMultipartContent(request)) {
            log.info("*****文件上传");

            jsonObject.addProperty("error", 0);
            jsonObject.addProperty("url", "http://image.baidu.com/search/detail?ct=503316480&z=undefined&tn=baiduimagedetail&ipn=d&word=kindeditor&step_word=&ie=utf-8&in=&cl=2&lm=-1&st=undefined&cs=1315447497,3169870753&os=3070755073,2276264566&simid=4193554957,645081131&pn=0&rn=1&di=107660090010&ln=1115&fr=&fmq=1451315447605_R&ic=undefined&s=undefined&se=&sme=&tab=0&width=&height=&face=undefined&is=&istype=0&ist=&jit=&bdtype=0&gsm=0&objurl=http%3A%2F%2Fwww.esoyu.com%2Fsoft%2FUploadPic%2F2013-10%2FKindEditor.jpg");
        } else {
            jsonObject.addProperty("error", 1);
            jsonObject.addProperty("message", "不是多媒体");
        }

        out.write(jsonObject.toString());
    }
}
