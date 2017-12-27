package com.simuwang.xxx.comm;


/**
 * function :  接口地址常量池.
 *
 * <p>
 * Created by Leo on 2015/12/31.
 */
public class URLConst {
    /** Host地址 */
    private static String URL_BASE;

    static {
        // 生产环境
        if (Configs.STATE == Configs.URL_RELEASE) {
            URL_BASE = "https://ppwapp.simuwang.com";
        }
        // 预生产环境
        else if (Configs.STATE == Configs.URL_PRE_RELEASE) {
            URL_BASE = "http://ppwapp-pre.simuwang.com";
        }
        // 测试环境
        else if (Configs.STATE == Configs.URL_TEST) {
            URL_BASE = "http://ppwapp-test.simuwang.com";
        }
        // 开发环境
        else if (Configs.STATE == Configs.URL_DEV) {
            URL_BASE = "http://ppwapp-dev.simuwang.com";
        }
        //unknow
        else {
            throw new IllegalArgumentException("I don't know what the state is now. please check Configs.STATE");
        }
        android.util.Log.e("smpp", "state:" + (Configs.STATE == 0 ? "develop" : Configs.STATE == 1 ? "test" : Configs.STATE == 2 ? "preOnline" : Configs.STATE == 3 ? "online" : "unknow"));
    }

    //-----------------------------------------------------业务接口

    /** 启动 广告页 信息查询 */
    public static String SPLASH_AD_INFO = URL_BASE + "/Account/advertisingGuideData";

}