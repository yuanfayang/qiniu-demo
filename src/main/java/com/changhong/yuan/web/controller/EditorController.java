package com.changhong.yuan.web.controller;

import com.changhong.yuan.web.qiniu.MyRet;
import com.changhong.yuan.web.qiniu.QiniuCloudConfig;
import com.changhong.yuan.web.qiniu.UploadTokenHelper;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @authr: Fayang Yuan
 * @Date: 2015/12/28
 * @Time: 22:25
 * @Email: flyyuanfayang@sina.com
 * @Description: 富文本编辑器Controller
 */
@Controller
public class EditorController {
    private final Logger logger = LoggerFactory.getLogger(EditorController.class);

    @RequestMapping(value = "/editor", method = RequestMethod.GET)
    public String view(Model model) {
        model.addAttribute("token", UploadTokenHelper.buildFrontEndUploadToken());
        return "editor";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(@RequestParam(value = "file")MultipartFile file,
                       HttpServletRequest request,
                       HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (ServletFileUpload.isMultipartContent(request)) {
            logger.info("文件上传");

            UploadManager uploadManager = new UploadManager();
            String uploadToken = UploadTokenHelper.buildBackEndToken();
            try {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
                    String time = sdf.format(new Date());
                    Response qiniuResponse = uploadManager.put(file.getBytes(),time,uploadToken);
                    logger.info("上传响应"+qiniuResponse);
                    MyRet myRet = qiniuResponse.jsonToObject(MyRet.class);
                    logger.info("返回数据"+JSONObject.fromObject(myRet));

                    jsonObject.put("error", 0);
                    jsonObject.put("url", QiniuCloudConfig.domain+"/"+myRet.key);
                } catch (QiniuException e) {
                    Response res = e.response;

                    logger.error("上传文件出错"+res.toString());
                    try {
                        logger.error(res.bodyString());

                        jsonObject.put("error", 1);
                        jsonObject.put("message", res.bodyString());
                    }catch (QiniuException e1){
                        e.printStackTrace();

                        jsonObject.put("error", 1);
                        jsonObject.put("message", e1.getStackTrace());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();

                jsonObject.put("error", 1);
                jsonObject.put("message", "解析文件出错");
            }


        } else {
            jsonObject.put("error", 1);
            jsonObject.put("message","请上传文件");
        }

        out.write(jsonObject.toString());
        out.close();
    }
}
