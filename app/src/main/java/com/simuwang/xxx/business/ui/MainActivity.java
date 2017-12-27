package com.simuwang.xxx.business.ui;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leo618.utils.UIUtil;
import com.simuwang.xxx.MyApp;
import com.simuwang.xxx.R;
import com.simuwang.xxx.base.BaseActivity;

import butterknife.BindView;

/**
 * function:主页
 *
 * <p>
 * Created by Leo on 2017/12/20.
 */
public class MainActivity extends BaseActivity {
    @Override
    protected int contentViewLayout() {
        return R.layout.activity_main;
    }

    @BindView(R.id.mTitleBackButton)
    ImageView mTitleBackButton;
    @BindView(R.id.mTitleTxt)
    TextView  mTitleTxt;

    @Override
    protected void initView() {
        mTitleBackButton.setVisibility(View.GONE);
        mTitleTxt.setText(R.string.app_name);
    }

    @Override
    protected void initData() {
        //TODO load data here
        UIUtil.showToastShort("Hello Android", Gravity.BOTTOM);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    private long touchTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long currentTime = System.currentTimeMillis();
            long waitTime    = 2000;
            if ((currentTime - touchTime) >= waitTime) {
                UIUtil.showToastShort("再按一次退出", Gravity.CENTER);
                touchTime = currentTime;
            } else {
                MyApp.getApplication().removeAll();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
