package com.simuwang.xxx.comm;


/**
 * function : 通用配置类.
 * <p>
 * Created by lzj on 2017/12/31.
 */
public final class Configs {
    /** 接口环境： 开发环境 */
    public static final int URL_DEV         = 0;
    /** 接口环境： 测试环境 */
    public static final int URL_TEST        = 1;
    /** 接口环境： 预生产环境 */
    public static final int URL_PRE_RELEASE = 2;
    /** 接口环境： 生产环境 */
    public static final int URL_RELEASE     = 3;

    /** 当前APK模式 0-开发模式; 1-测试模式; 2-预生产模式; 3-生产模式 */
    public static int STATE = URL_DEV;

    /** 调试开关 */
    public static boolean DEBUG = true;

    static {
        //开发模式
        if (URL_DEV == STATE) {
            DEBUG = true;
        }
        //测试环境
        else if (URL_TEST == STATE) {
            DEBUG = false;
        }
        //预生产模式
        else if (URL_PRE_RELEASE == STATE) {
            DEBUG = false;
        }
        //生产模式
        else if (URL_RELEASE == STATE) {
            DEBUG = false;
        }
        //未知
        else {
            throw new RuntimeException("STATE is wrong!!!");
        }
    }

    /** 用户token */
    public static String token;
}
