package com.simuwang.xxx.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.simuwang.xxx.MyApp;
import com.simuwang.xxx.manager.EventManager;
import com.simuwang.xxx.manager.SystemBarTintManager;
import com.simuwang.xxx.utils.Logg;
import com.simuwang.xxx.utils.StringUtil;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.ButterKnife;


/**
 * function:android 系统中的四大组件之一Activity基类.
 * <p>
 * Created by Leo on 2017/12/20.
 */
@SuppressWarnings("unused")
public abstract class BaseActivity extends AppCompatActivity {
    /** 日志输出标志,当前类的类名 */
    protected final String TAG = this.getClass().getSimpleName();

    /** 整个应用Applicaiton */
    private MyApp mApplication;
    /** 当前Activity的弱引用，防止内存泄露 */
    private WeakReference<Activity> activityWeakReference = null;
    /** 系统状态栏管理类 */
    private SystemBarTintManager mSysbarTintManager;

    /**
     * 设置视图ID
     */
    protected abstract int contentViewLayout();

    /**
     * 初始化视图
     */
    protected abstract void initView();

    /**
     * 处理业务逻辑
     */
    protected abstract void initData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logg.v(TAG, TAG + "-->onCreate");
        mApplication = (MyApp) this.getApplication();
        activityWeakReference = new WeakReference<>(this);
        mApplication.pushTask(activityWeakReference);
        int contentViewLayout = this.contentViewLayout();
        if (contentViewLayout > 0) setContentView(contentViewLayout);
        if (this.enableEventTrans()) EventManager.register(this);
        if (this.enableButterKnife()) ButterKnife.bind(this);
        this.initView();
        this.initData();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Logg.v(TAG, TAG + "-->onPostCreate");
        if (this.enableSystemBarManager()) {
            initSystemBarManager();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Logg.v(TAG, TAG + "-->onNewIntent intent=" + intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Logg.v(TAG, TAG + "-->onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logg.v(TAG, TAG + "-->onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logg.v(TAG, TAG + "-->onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logg.v(TAG, TAG + "-->onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logg.v(TAG, TAG + "-->onStop");
    }

    @Override
    protected void onDestroy() {
        if (this.enableEventTrans()) EventManager.unregister(this);
        if (mApplication != null && activityWeakReference != null) {
            mApplication.removeTask(activityWeakReference);
        }
        super.onDestroy();
        Logg.v(TAG, TAG + "-->onDestroy");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FragmentManager supportFragmentManager = this.getSupportFragmentManager();
        List<Fragment>  fragments              = supportFragmentManager.getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                fragment.onActivityResult(requestCode & 0xffff, resultCode, data);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        Logg.v(TAG, TAG + "-->onActivityResult: requestCode=" + requestCode + " , resultCode=" + resultCode + " ,data=" + data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        FragmentManager supportFragmentManager = this.getSupportFragmentManager();
        List<Fragment>  fragments              = supportFragmentManager.getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        Logg.v(TAG, TAG + "-->onRequestPermissionsResult: requestCode=" + requestCode
                + " , permissions=" + StringUtil.strArrString(permissions) + " , grantResults=" + StringUtil.strArrInt(grantResults));
    }

    /** 是否需要处理软键盘失去焦点隐藏的事件 */
    protected boolean enableDispatchTouchEventOnSoftKeyboard() {
        return true;
    }

    /** 是否启用SystemBarManager */
    protected boolean enableSystemBarManager() {
        return true;
    }

    /** 是否禁用基类事件分发注册和反注册 */
    protected boolean enableEventTrans() {
        return true;
    }

    /** 是否禁用基类ButterKnife注册和反注册 */
    protected boolean enableButterKnife() {
        return true;
    }

    //处理失去焦点软键盘隐藏事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!this.enableDispatchTouchEventOnSoftKeyboard()) {
            return super.dispatchTouchEvent(ev);
        }
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    /**
     * 初始化状态栏管理器
     */
    private void initSystemBarManager() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mSysbarTintManager = new SystemBarTintManager(this);
        mSysbarTintManager.setStatusBarTintEnabled(true);
    }

    /** 获取系统状态栏管理器 可用于设置状态栏颜色透明度等 */
    public SystemBarTintManager getSysbarTintManager() {
        if (mSysbarTintManager == null) {
            initSystemBarManager();
        }
        return mSysbarTintManager;
    }

    /**
     * 点击输入框外需要隐藏键盘
     */
    private boolean isShouldHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop); // 获取输入框当前的location位置
            int left   = leftTop[0];
            int top    = leftTop[1];
            int bottom = top + v.getHeight();
            int right  = left + v.getWidth();
            // 点击的是输入框区域，保留点击EditText的事件
            return !(ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom);
        }
        return false;
    }

}
