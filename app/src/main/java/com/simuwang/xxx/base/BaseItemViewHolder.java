package com.simuwang.xxx.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * function:RecyclerView的通用基类适配器需要用到的基类ViewHolder
 *
 * <p>
 * Created by Leo on 2017/3/10.
 */
public abstract class BaseItemViewHolder extends RecyclerView.ViewHolder {

    public BaseItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
