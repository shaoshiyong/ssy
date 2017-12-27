package com.simuwang.xxx.manager;

import android.Manifest;
import android.text.TextUtils;

import com.leo618.mpermission.MPermission;
import com.leo618.utils.FileStorageUtil;
import com.leo618.utils.SPUtil;
import com.simuwang.xxx.MyApp;
import com.simuwang.xxx.bean.SplashADBean;
import com.simuwang.xxx.comm.Const;
import com.simuwang.xxx.comm.URLConst;
import com.simuwang.xxx.interf.IRequestCallback;
import com.simuwang.xxx.manager.net.NetManager;
import com.simuwang.xxx.utils.Logg;
import com.simuwang.xxx.utils.StringUtil;

import java.util.HashMap;

/**
 * function:简易操作
 *
 * <p>
 * Created by Leo on 2017/12/21.
 */
public class EasyAction {

    //查询广告信息
    public static void queryAdInfo() {
        NetManager.post(URLConst.SPLASH_AD_INFO, new HashMap<>(),
                new IRequestCallback<SplashADBean>() {
                    @Override
                    public void onFailure(Exception e) {
                        Logg.e("ad", "failed: e=" + (e == null ? "null" : e.getMessage()));
                    }

                    @Override
                    public void onSuccess(SplashADBean splashADBean) {
                        SplashADBean.DataBean adInfoData = splashADBean.getData();
                        if (adInfoData == null || (TextUtils.isEmpty(adInfoData.getRoute_key()) && TextUtils.isEmpty(adInfoData.getRoute_img()))) {
                            SPUtil.putString(Const.KEY_SPLASH_AD_INFO, null);
                            return;
                        }
                        SplashADBean adInfoDataOld = JsonManager.parseObject(SPUtil.getString(Const.KEY_SPLASH_AD_INFO, null), SplashADBean.class);
                        SPUtil.putString(Const.KEY_SPLASH_AD_INFO, JsonManager.toJSONString(splashADBean));
                        if (adInfoDataOld != null && adInfoDataOld.getData() != null) {
                            SplashADBean.DataBean dataOld = adInfoDataOld.getData();
                            //广告ID和广告图片地址与本地缓存比较均未发生变化，则认为不需要从新下载广告图片
                            if (TextUtils.equals(dataOld.getRoute_img(), adInfoData.getRoute_img())
                                    && TextUtils.equals(dataOld.getRoute_img_id(), adInfoData.getRoute_img_id()))
                                return;
                        }
                        String[] permissions = new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                        };
                        if (MPermission.hasPermissions(MyApp.getApplication(), permissions)) {
                            String filePath = FileStorageUtil.getPictureDirPath() + StringUtil.getFileNameWithSuffInUrl(adInfoData.getRoute_img());
                            FileDownloader.downloadFile(adInfoData.getRoute_img(), filePath);
                        }
                    }
                });
    }
}
