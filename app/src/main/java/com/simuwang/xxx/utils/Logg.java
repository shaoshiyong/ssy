package com.simuwang.xxx.utils;


import com.simuwang.xxx.comm.Configs;

/**
 * function : 日志输出.
 *
 * <p>
 * Created by Leo on 2015/12/31.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class Logg {
    private static final boolean DEBUG       = Configs.DEBUG;
    private static final String  TAG_DEFAULT = "TAG_DEFAULT";

    public static void v(String content) {
        v(TAG_DEFAULT, content);
    }

    public static void v(String tag, String content) {
        if (!DEBUG) return;
        android.util.Log.v(tag, content == null ? "null" : content);
    }

    public static void w(String content) {
        w(TAG_DEFAULT, content);
    }

    public static void w(String tag, String content) {
        if (!DEBUG) return;
        android.util.Log.w(tag, content == null ? "null" : content);
    }

    public static void i(String content) {
        i(TAG_DEFAULT, content);
    }

    public static void i(String tag, String content) {
        if (!DEBUG) return;
        android.util.Log.i(tag, content == null ? "null" : content);
    }


    public static void d(String content) {
        d(TAG_DEFAULT, content);
    }

    public static void d(String tag, String content) {
        if (!DEBUG) return;
        android.util.Log.d(tag, content == null ? "null" : content);
    }

    public static void e(String content) {
        e(TAG_DEFAULT, content);
    }

    public static void e(String tag, String content) {
        if (!DEBUG) return;
        android.util.Log.e(tag, content == null ? "null" : content);
    }

    public static void e(String content, Throwable e) {
        e(TAG_DEFAULT, content, e);
    }

    public static void e(String tag, String content, Throwable e) {
        if (!DEBUG) return;
        android.util.Log.e(tag, content == null ? "null" : content, new Throwable(e));
    }

}
