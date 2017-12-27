package com.simuwang.xxx.manager.net;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.leo618.utils.NetworkUtil;
import com.simuwang.xxx.MyApp;
import com.simuwang.xxx.base.BaseBean;
import com.simuwang.xxx.comm.Configs;
import com.simuwang.xxx.comm.Const;
import com.simuwang.xxx.interf.IRequestCallback;
import com.simuwang.xxx.manager.JsonManager;
import com.simuwang.xxx.utils.Logg;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;


/**
 * function : 网络请求类（单例模式）.所有请求均是异步操作，回调接口运行在UI线程.
 *
 * <p>
 * Created by Leo on 2015/11/3.
 */
@SuppressWarnings({"unused", "unchecked", "WeakerAccess"})
public class NetManager {
    private static final String TAG = "NetManager";

    private static final AtomicReference<NetManager> INSTANCE = new AtomicReference<>();
    private final Map<String, IRequestCallback> mCallbackMap;

    private static NetManager getInstance() {
        for (; ; ) {
            NetManager netManager = INSTANCE.get();
            if (netManager != null) return netManager;
            netManager = new NetManager();
            if (INSTANCE.compareAndSet(null, netManager)) return netManager;
        }
    }

    //--------------------static method-----start

    /** GET */
    public static void get(String url, Map<String, String> paramMap, IRequestCallback iRequestCallback) {
        getInstance().get(NET_TAG(), url, paramMap, iRequestCallback);
    }

    /** POST */
    public static void post(String url, Map<String, String> paramMap, IRequestCallback iRequestCallback) {
        getInstance().post(NET_TAG(), url, paramMap, iRequestCallback);
    }

    /** download */
    public static void download(String url, String filePath, IRequestCallback iRequestCallback) {
        getInstance().download(NET_TAG(), url, filePath, iRequestCallback);
    }

    /** upload */
    public static void upload(String url, Map<String, String> paramMap, Map<String, File> fileMap, IRequestCallback iRequestCallback) {
        getInstance().upload(NET_TAG(), url, paramMap, fileMap, iRequestCallback);
    }
    //--------------------static method-----end


    /**
     * 统一httpGet请求入口
     *
     * @param tag              请求类型标记,用于用户处理网络请求比如取消请求
     * @param url              请求完整地址url
     * @param paramMap         参数集合 key=value键值对
     * @param iRequestCallback 回调接口
     */
    private void get(String tag, String url, Map<String, String> paramMap, IRequestCallback iRequestCallback) {
        if (!NetworkUtil.isNetworkConnected(MyApp.getApplication()) && iRequestCallback != null) {
            iRequestCallback.onFailure(new RuntimeException("网络似乎有问题"));
            return;
        }
        if (paramMap == null) paramMap = new LinkedHashMap<>();
        if (null == mCallbackMap.get(tag)) mCallbackMap.put(tag, iRequestCallback);
        url = handleCommonParam(url, paramMap);
        if (!url.endsWith("?")) url += "?";//check if end with ?
        StringBuilder urlBuilder = new StringBuilder(url);
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        if (paramMap.size() > 0) {
            url = urlBuilder.toString().substring(0, url.length() - 1);//remove last &
        }
        log_url("get", tag, url, paramMap);
        OkHttpClientWrap.instance().get(url, new NetResponseCallback(tag), tag);
    }

    /**
     * 统一httpPost请求入口
     *
     * @param tag              请求类型标记
     * @param url              请求完整地址url
     * @param iRequestCallback 回调接口
     * @param paramMap         参数集合
     */
    public void post(String tag, String url, Map<String, String> paramMap, IRequestCallback iRequestCallback) {
        if (!NetworkUtil.isNetworkConnected(MyApp.getApplication()) && iRequestCallback != null) {
            iRequestCallback.onFailure(new RuntimeException("网络似乎有问题"));
            return;
        }
        if (paramMap == null) paramMap = new LinkedHashMap<>();
        if (null == mCallbackMap.get(tag)) mCallbackMap.put(tag, iRequestCallback);
        url = handleCommonParam(url, paramMap);
        log_url("post", tag, url, paramMap);
        OkHttpClientWrap.instance().post(url, paramMap, new NetResponseCallback(tag), tag);
    }

    /** 打印url及参数Log信息 */
    private void log_url(String actionWhat, String tag, String url, Map<String, String> paramMap) {
        if (!Configs.DEBUG) return;
        if (paramMap == null || paramMap.size() == 0) return;
        StringBuilder params = new StringBuilder(" params: ");
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            params.append(entry.getKey()).append("=").append(entry.getValue()).append(",");
        }
        Logg.d(TAG, "(" + tag + ")" + actionWhat + ": " + url + " ; " + params.substring(0, params.length() - 1));
    }

    /**
     * 统一download请求入口
     *
     * @param tag              请求标记
     * @param url              请求地址
     * @param filePath         下载完成后文件保存的文件路径
     * @param iRequestCallback 请求回调
     */
    public void download(String tag, String url, String filePath, IRequestCallback iRequestCallback) {
        if (!NetworkUtil.isNetworkConnected(MyApp.getApplication()) && iRequestCallback != null) {
            iRequestCallback.onFailure(new RuntimeException("网络似乎有问题"));
            return;
        }
        if (null == mCallbackMap.get(tag)) {
            mCallbackMap.put(tag, iRequestCallback);
        }
        OkHttpClientWrap.instance().download(url, filePath, new FileDownloadCallback(tag), tag);
    }

    /**
     * 统一upload请求入口
     *
     * @param tag              请求标记
     * @param url              请求地址
     * @param paramMap         上传携带的请求参数
     * @param fileMap          上传文件集合
     * @param iRequestCallback 请求回调
     */
    public void upload(String tag, String url, Map<String, String> paramMap, Map<String, File> fileMap, IRequestCallback iRequestCallback) {
        if (!NetworkUtil.isNetworkConnected(MyApp.getApplication()) && iRequestCallback != null) {
            iRequestCallback.onFailure(new RuntimeException("网络似乎有问题"));
            return;
        }
        if (null == mCallbackMap.get(tag)) {
            mCallbackMap.put(tag, iRequestCallback);
        }
        String newUrl = handleCommonParam(url, paramMap);
        OkHttpClientWrap.instance().upload(newUrl, paramMap, fileMap, new UploadResponseCallback(tag), tag);
    }

    //添加通用字段值
    private String handleCommonParam(String url, @NonNull Map<String, String> params) {
        //TODO change here
        if (!TextUtils.isEmpty(Configs.token)) params.put("token", Configs.token);
        params.put("type", "JSON");
        params.put("app_install_version", "4.0");
        params.put("device_uuid", UUID.randomUUID().toString().replace("-", ""));
        params.put("app_type", "3");
        return url;
    }

    /** 文件下载拦截回调  ,UI Thread */
    private class FileDownloadCallback extends IRequestCallback<File> {
        private final String tag;

        public FileDownloadCallback(String tag) {
            this.tag = tag;
        }

        @Override
        public void onStart() {
            Logg.v(TAG, "start");
            if (mCallbackMap.get(tag) != null) {
                mCallbackMap.get(tag).onStart();
            }
        }

        @Override
        public void onProgressUpdate(long writedSize, long totalSize, boolean completed) {
            IRequestCallback callback = mCallbackMap.get(tag);
            if (callback != null) {
                callback.onProgressUpdate(writedSize, totalSize, completed);
            }
        }

        @Override
        public void onSuccess(File file) {
            if (null == file) {
                Logg.d(TAG, "(" + tag + ")" + "success: file is null.");
                this.onFailure(null);
                return;
            }

            IRequestCallback callback = mCallbackMap.get(tag);
            if (callback == null) {
                return;
            }
            callback.onSuccess(file);
            removeRequestCallback(tag);
        }

        @Override
        public void onFailure(Exception e) {
            Logg.e(TAG, "failure:" + (e == null ? "nothing" : e.getMessage()));
            if (mCallbackMap.get(tag) != null) {
                mCallbackMap.get(tag).onFailure(e);
                removeRequestCallback(tag);
            }
        }
    }

    /** 网络请求拦截回调  ,UI Thread */
    private class NetResponseCallback extends IRequestCallback<String> {
        private final String tag;

        public NetResponseCallback(String tag) {
            this.tag = tag;
        }

        @Override
        public void onStart() {
            Logg.v(TAG, "start");
            if (mCallbackMap.get(tag) != null) {
                mCallbackMap.get(tag).onStart();
            }
        }

        @Override
        public void onSuccess(String response) {
            Logg.d(TAG, "(" + tag + ")" + "success: " + response);
            if (null == response) {
                Logg.d(TAG, "(" + tag + ")" + "success: response is null.");
                this.onFailure(null);
                return;
            }

            IRequestCallback callBack = mCallbackMap.get(tag);
            if (callBack == null) return;
            if (callBack.mClassType == String.class) {
                callBack.onSuccess(response);
            } else {
                BaseBean baseBean = JsonManager.parseObject(response, BaseBean.class);
                if (baseBean == null) {
                    Logg.e(TAG, "(" + tag + ")" + "success: BaseBean is null.");
                    callBack.onFailure(null);
                    return;
                }
                if (baseBean.getStatus() == Const.RESP_OK) { // ok
                    final Object parseObject = JsonManager.parseType(response, callBack.mClassType);
                    if (parseObject == null) {
                        Logg.e(TAG, "(" + tag + ")" + "success: parseObject is null.");
                        callBack.onFailure(new RuntimeException("数据解析错误"));
                    } else {
                        callBack.onSuccess(parseObject);
                    }
                } else if (baseBean.getStatus() == Const.RESP_RE_LOGIN) { // reLogin
                    Logg.e(TAG, "(" + tag + ")" + "success: need reLogin.");
                    callBack.onFailure(new RuntimeException("请重新登录"));
                    //TODO relogin
                } else {
                    Exception statusException = new RuntimeException(baseBean.toString());
                    Logg.e(TAG, "(" + tag + ")" + "success: Exception = " + statusException.toString());
                    callBack.onFailure(statusException);
                }
            }
            removeRequestCallback(tag);
        }

        @Override
        public void onFailure(Exception e) {
            Logg.e(TAG, "(" + tag + ")" + "failure: " + (e == null ? "nothing" : e.getMessage()));
            if (mCallbackMap.get(tag) != null) {
                mCallbackMap.get(tag).onFailure(e);
                removeRequestCallback(tag);
            }
        }
    }

    /** 上传文件拦截回调  ,UI Thread */
    private class UploadResponseCallback extends IRequestCallback<String> {
        private final String tag;

        public UploadResponseCallback(String tag) {
            this.tag = tag;
        }

        @Override
        public void onStart() {
            Logg.v(TAG, "(" + tag + ")" + "start");
            if (mCallbackMap.get(tag) != null) {
                mCallbackMap.get(tag).onStart();
            }
        }

        @Override
        public void onProgressUpdate(long writedSize, long totalSize, boolean completed) {
            IRequestCallback callBack = mCallbackMap.get(tag);
            if (callBack == null) return;
            callBack.onProgressUpdate(writedSize, totalSize, completed);
        }

        @Override
        public void onSuccess(String response) {
            Logg.d(TAG, "(" + tag + ")" + "success: " + response);
            IRequestCallback callBack = mCallbackMap.get(tag);
            if (callBack == null) return;
            if (callBack.mClassType == String.class) {
                callBack.onSuccess(response);
            } else {
                Object parseObject = JsonManager.parseType(response, callBack.mClassType);
                if (parseObject == null) {
                    Logg.e(TAG, "(" + tag + ")" + "success: parseObject is null.");
                    callBack.onFailure(new RuntimeException("数据解析错误"));
                } else {
                    callBack.onSuccess(parseObject);
                }
            }
            removeRequestCallback(tag);
        }

        @Override
        public void onFailure(Exception e) {
            Logg.e(TAG, "(" + tag + ")" + "failure: " + (e == null ? "nothing" : e.getMessage()));
            if (mCallbackMap.get(tag) != null) {
                mCallbackMap.get(tag).onFailure(e);
                removeRequestCallback(tag);
            }
        }

    }

    /** 移除指定类型的回调接口 */
    public void removeRequestCallback(String tag) {
        IRequestCallback iRequestCallBack = mCallbackMap.get(tag);
        if (null != iRequestCallBack) {
            mCallbackMap.remove(tag);
        }
    }

    private NetManager() {
        mCallbackMap = new ConcurrentHashMap<>();
    }

    /** 产生唯一标记tag */
    private static String NET_TAG() {
        String str = String.valueOf(System.currentTimeMillis());
        return str.substring(7, str.length());
    }

}
