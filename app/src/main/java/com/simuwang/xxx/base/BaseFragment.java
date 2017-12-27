package com.simuwang.xxx.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simuwang.xxx.manager.EventManager;
import com.simuwang.xxx.utils.Logg;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * function : Fragment基类(兼容低版本).
 * <p>
 * Created by Leo on 2017/12/31.
 */
@SuppressWarnings({"unused", "deprecation"})
public abstract class BaseFragment extends Fragment {
    /** 日志输出标志 **/
    protected final String TAG = this.getClass().getSimpleName();
    /** fragment根布局 */
    protected View     mRootView;
    //ButterKnife Binder
    private   Unbinder mUnbinder;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setUserVisibleHint(false);
        Logg.v(TAG, TAG + "-->onAttach");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Logg.v(TAG, TAG + "-->onActivityCreated");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logg.v(TAG, TAG + "-->onCreate");
    }

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logg.v(TAG, TAG + "-->onCreateView");
        if (null != mRootView) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (null != parent) {
                parent.removeView(mRootView);
            }
        }
        mRootView = inflater.inflate(contentViewLayout(), container, false);
        if (this.enableEventTrans()) EventManager.register(this);
        if (this.enableButterKnife()) mUnbinder = ButterKnife.bind(this, mRootView);
        this.initView();
        mIsPrepared = true;
        return mRootView;
    }

    private boolean mIsInited;
    private boolean mIsPrepared;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Logg.v(TAG, TAG + "-->onViewCreated");
        lazyLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) lazyLoad();
    }

    //懒加载
    private void lazyLoad() {
        Logg.v(TAG, TAG + "-->lazyLoad");
        if (getUserVisibleHint() && mIsPrepared && !mIsInited) {
            Logg.v(TAG, TAG + "-->initData");
            mRootView.post(this::initData);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Logg.v(TAG, TAG + "-->onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        Logg.v(TAG, TAG + "-->onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        setUserVisibleHint(false);
        Logg.v(TAG, TAG + "-->onPause");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logg.v(TAG, TAG + "-->onDestroyView");
        if (mUnbinder != null) mUnbinder.unbind();
        if (this.enableEventTrans()) EventManager.unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logg.v(TAG, TAG + "-->onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Logg.v(TAG, TAG + "-->onDetach");
    }

    @SuppressWarnings("unchecked")
    public final <T extends View> T findViewById(@IdRes int id) {
        return (T) mRootView.findViewById(id);
    }

    /** 是否禁用基类事件分发注册和反注册 */
    protected boolean enableEventTrans() {
        return true;
    }

    /** 是否禁用基类ButterKnife注册和反注册 */
    protected boolean enableButterKnife() {
        return true;
    }

    public View getRootView() {
        return mRootView;
    }
}
