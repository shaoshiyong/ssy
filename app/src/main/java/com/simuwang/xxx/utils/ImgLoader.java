package com.simuwang.xxx.utils;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.simuwang.xxx.MyApp;
import com.simuwang.xxx.R;


/**
 * function : 图片加载工具类.
 *
 * <p>
 * Created by Leo on 2016/1/28.
 */
@SuppressWarnings("ALL")
public class ImgLoader {
    private static final String TAG = "ImgLoader";

    private static final int defaultPlaceholderForImg    = R.drawable.default_placeholder_img;
    private static final int defaultPlaceholderForAvatar = R.drawable.default_placeholder_avatar;

    private static RequestManager loader() {
        return Glide.with(MyApp.getApplication());
    }

    /**
     * 加载图片(加载完整地址包括网络地址和本地地址)
     *
     * @param imgPath         图片地址 eg: http://www.ypwl.com/up_files/picture.jpg , /storage0/DCIM/picture.jpg
     * @param targetImageView 目标ImageView
     */
    public static void loadImg(String imgPath, ImageView targetImageView) {
        loadImg(imgPath, targetImageView, 0);
    }

    /**
     * 加载图片(加载完整地址包括网络地址和本地地址)
     *
     * @param imgPath          图片地址 eg: http://www.ypwl.com/up_files/picture.jpg , /storage0/DCIM/picture.jpg
     * @param targetImageView  目标ImageView
     * @param placeholderResId 默认图片资源id
     */
    public static void loadImg(String imgPath, ImageView targetImageView, int placeholderResId) {
        if (TextUtils.isEmpty(imgPath)) {
            loader().load(placeholderResId > 0 ? placeholderResId : defaultPlaceholderForImg).into(targetImageView);
            return;
        }
        if (!imgPath.startsWith("http") && !imgPath.startsWith("file://")) {
            imgPath = "file://" + imgPath;
        }
        RequestOptions options = new RequestOptions()
                .placeholder(placeholderResId > 0 ? placeholderResId : defaultPlaceholderForImg)
                .error(placeholderResId > 0 ? placeholderResId : defaultPlaceholderForImg)
                .priority(Priority.HIGH);
        loader().load(imgPath).apply(options).into(targetImageView);
    }

    /**
     * 加载头像(加载完整地址包括网络地址和本地地址)
     *
     * @param avatarPath      avatar图片地址 eg: http://www.ypwl.com/up_files/user_avatar.jpg , /storage0/DCIM/avatar.jpg
     * @param targetImageView 目标ImageView
     */
    public static void loadAvatar(String avatarPath, ImageView targetImageView) {
        loadAvatar(avatarPath, targetImageView, 0);
    }

    /**
     * 加载头像(加载完整地址包括网络地址和本地地址)
     *
     * @param avatarPath       avatar图片地址 eg: http://www.ypwl.com/up_files/user_avatar.jpg , /storage0/DCIM/avatar.jpg
     * @param targetImageView  目标ImageView
     * @param placeholderResId 默认图片id
     */
    public static void loadAvatar(String avatarPath, ImageView targetImageView, int placeholderResId) {
        if (TextUtils.isEmpty(avatarPath)) {
            loader().load(placeholderResId > 0 ? placeholderResId : defaultPlaceholderForAvatar).into(targetImageView);
            return;
        }
        if (!avatarPath.startsWith("http") && !avatarPath.startsWith("file://")) {
            avatarPath = "file://" + avatarPath;
        }
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeholderResId > 0 ? placeholderResId : defaultPlaceholderForAvatar)
                .error(placeholderResId > 0 ? placeholderResId : defaultPlaceholderForAvatar)
                .priority(Priority.HIGH);
        loader().load(avatarPath).apply(options).into(targetImageView);
    }
}
