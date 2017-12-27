package com.simuwang.xxx.business.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.leo618.splashpermissionsauth.SplashAuthUI;
import com.leo618.utils.AndroidUtilsCore;
import com.leo618.utils.DateTimeUtil;
import com.leo618.utils.FileStorageUtil;
import com.leo618.utils.NumberUtil;
import com.leo618.utils.SPUtil;
import com.leo618.utils.UIUtil;
import com.simuwang.xxx.R;
import com.simuwang.xxx.bean.SplashADBean;
import com.simuwang.xxx.comm.Const;
import com.simuwang.xxx.manager.EasyAction;
import com.simuwang.xxx.manager.FileDownloader;
import com.simuwang.xxx.manager.JsonManager;
import com.simuwang.xxx.utils.StringUtil;
import com.simuwang.xxx.widget.CountDownCircleView;

/**
 * function:启动页
 *
 * <p></p>
 * Created by Leo on 2017/12/20.
 */
public class SplashActivity extends AppCompatActivity {
    // 同一天显示广告的次数最大值
    private static final int TIME_PER_DAY     = 3;
    // 广告显示时长
    private static final int TIME_SHOW_AD     = 5000;
    // 延时几秒进入主页
    private static final int TIME_DELAY_ENTER = 2000;

    private ImageView           mImgUI;
    private CountDownCircleView mTimeUI;

    private long mStartTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mImgUI = findViewById(R.id.img);
        mTimeUI = findViewById(R.id.time);
        mImgUI.setTag(false);
        mStartTime = System.currentTimeMillis();
        EasyAction.queryAdInfo();
        SplashAuthUI.launch(this, 100, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                AndroidUtilsCore.initAfterCheckPermissions();
                long diffTime = System.currentTimeMillis() - mStartTime;
                UIUtil.postDelayed(mDelayRun, diffTime < TIME_DELAY_ENTER ? (TIME_DELAY_ENTER - diffTime) : 0);
            } else {
                UIUtil.showToastShort("在使用APP之前，请您允许必要的权限");
                finish();
            }
        }
    }

    //延迟执行
    private Runnable mDelayRun = () -> {
        //不是同一天了，刷新一天显示次数的记录
        if (!DateTimeUtil.isToday(SPUtil.getLong(Const.KEY_TIMESTAMP_LAST_LAUNCH, 0L))) {
            SPUtil.putInt(Const.KEY_COUNT_SHOW_AD_IN_ONE_DAY, 0);
        }
        //同一天，检查次数是否达到上限，达到则jump，未达到则显示广告
        else {
            int countShowAd = SPUtil.getInt(Const.KEY_COUNT_SHOW_AD_IN_ONE_DAY, 0);
            if (countShowAd >= TIME_PER_DAY) {
                jump();
                return;
            }
        }
        final SplashADBean adBean = JsonManager.parseObject(SPUtil.getString(Const.KEY_SPLASH_AD_INFO, null), SplashADBean.class);
        //无缓存广告信息
        if (adBean == null || adBean.getData() == null) {
            jump();
            return;
        }
        //有缓存广告信息
        final SplashADBean.DataBean adInfoData = adBean.getData();

        String filePath = FileStorageUtil.getPictureDirPath() + StringUtil.getFileNameWithSuffInUrl(adInfoData.getRoute_img());
        Bitmap bitmap   = BitmapFactory.decodeFile(filePath);
        // 检测配置情况，如需则下载图片
        if (bitmap == null) {
            FileDownloader.downloadFile(adInfoData.getRoute_img(), filePath);
            jump();
            return;
        }
        //显示图片 并开始倒计时
        mImgUI.setTag(true);
        mImgUI.setImageBitmap(bitmap);
        mTimeUI.setVisibility(View.VISIBLE);
        int timeSeconds = NumberUtil.parseInt(adInfoData.getRoute_show_second());
        mTimeUI.setTime(timeSeconds > 0 ? timeSeconds * 1000 : TIME_SHOW_AD).setCallback(this::jump).star();
        mImgUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimeUI != null) mTimeUI.stop();
                jump();
            }
        });
        // 设置广告展示计数加1
        SPUtil.putInt(Const.KEY_COUNT_SHOW_AD_IN_ONE_DAY, SPUtil.getInt(Const.KEY_COUNT_SHOW_AD_IN_ONE_DAY, 0) + 1);

    };

    private void jump() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        // cannot go back
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtil.putLong(Const.KEY_TIMESTAMP_LAST_LAUNCH, System.currentTimeMillis());
        UIUtil.removeCallbacksFromMainLooper(mDelayRun);
        mDelayRun = null;
    }
}
