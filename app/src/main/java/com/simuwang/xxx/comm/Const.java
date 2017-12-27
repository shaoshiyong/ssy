package com.simuwang.xxx.comm;

/**
 * function:常量池
 *
 * <p>
 * Created by Leo on 2017/12/21.
 */
public class Const {
    /** key : 记录启动页广告信息数据 */
    public static final String KEY_SPLASH_AD_INFO           = "key_splash_ad_info";
    /** key : 一天内显示广告的次数记录 */
    public static final String KEY_COUNT_SHOW_AD_IN_ONE_DAY = "key_count_show_ad_in_one_day";
    /** key : 记录上次启动APP时的时间戳 */
    public static final String KEY_TIMESTAMP_LAST_LAUNCH    = "key_timestamp_last_launch";

    //****************** 服务端状态码 ******************//
    /** 正常响应 */
    public static final int RESP_OK       = 0;
    /** 要求重登陆 */
    public static final int RESP_RE_LOGIN = 123;
}
