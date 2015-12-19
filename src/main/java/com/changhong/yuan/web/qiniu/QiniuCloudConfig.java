package com.changhong.yuan.web.qiniu;

import com.qiniu.util.Auth;
/**
 * @author ：Yuan Fayang
 * @package ; com.changhong.yuan.web.qiniu
 * @dateTime : 2015/12/19 11:58
 * @discription : 关于七牛云存的一些配置
 */
public class QiniuCloudConfig {

    public static final String ACCESS_KEY = "YCc_HMv4cnpehbsGaPu66l-e3Rmn0OZrAviDqb0l";
    public static final String SECRET_KEY = "9pjZsWv1LM0yJ6N6h5iAlYV6lda-XiTjEXApoRDa";
    public static final String bucket = "yuan-test";
    public static final String key = "yuan-test.jpg";
    public static final String domain = "7xpayt.com1.z0.glb.clouddn.com";

    public static Auth auth = null;
    static {
        auth = Auth.create(ACCESS_KEY,SECRET_KEY);
    }

    public QiniuCloudConfig() {
    }

    public static boolean isTravis(){
        return "travis".equals(System.getenv("QINIU_TEST_ENV"));
    }
}
