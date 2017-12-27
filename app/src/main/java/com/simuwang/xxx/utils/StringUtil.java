package com.simuwang.xxx.utils;

import android.text.TextUtils;

/**
 * function:字符串常用工具
 *
 * <p>
 * Created by Leo on 2017/12/20.
 */
public class StringUtil {

    public static String strArrString(String[] datas) {
        if (datas == null || datas.length == 0) return "";
        StringBuilder result = new StringBuilder("[");
        for (String data : datas) {
            result.append(data).append(",");
        }
        return result.substring(0, result.length() - 1) + "]";
    }

    public static String strArrInt(int[] datas) {
        if (datas == null || datas.length == 0) return "";
        StringBuilder result = new StringBuilder("[");
        for (int data : datas) {
            result.append(String.valueOf(data)).append(",");
        }
        return result.substring(0, result.length() - 1) + "]";
    }

    /**
     * 获取url地址的文件名和后缀名
     *
     * @return [0]:文件名，[1]：带点的后缀名
     */
    public static String[] getFileNameInUrl(String url) {
        String[] result = new String[2];
        if (!TextUtils.isEmpty(url)) {
            int lastIndexOfPoint = url.lastIndexOf(".");
            result[0] = url.substring(url.lastIndexOf("/") + 1, lastIndexOfPoint);
            result[1] = url.substring(lastIndexOfPoint, url.length());
        }
        return result;
    }

    /**
     * 获取url地址的带后缀名的文件名
     *
     * @return 文件名
     */
    public static String getFileNameWithSuffInUrl(String url) {
        String result = "";
        if (!TextUtils.isEmpty(url)) {
            result = url.substring(url.lastIndexOf("/") + 1, url.length());
        }
        return result;
    }
}
