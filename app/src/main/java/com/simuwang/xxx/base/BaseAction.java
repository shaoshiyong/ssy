package com.simuwang.xxx.base;

/**
 * function:UI的逻辑处理基类
 *
 * <p>
 * Created by Leo on 2017/12/31.
 */
@SuppressWarnings("unused")
public abstract class BaseAction {
    /** 日志输出标志,当前类的类名 */
    protected final String TAG = this.getClass().getSimpleName();

    public abstract void onDestroy();
}
