package com.simuwang.xxx.manager;

import com.leo618.downloader.Downloader;
import com.leo618.downloader.IDownloadCallback;
import com.simuwang.xxx.MyApp;
import com.simuwang.xxx.utils.Logg;

import java.io.File;

/**
 * function:文件下载管理器
 *
 * <p>
 * Created by Leo on 2017/4/13.
 */
public final class FileDownloader {
    private static final String TAG = "FileDownloader";

    private FileDownloader() {
    }

    /**
     * 下载图片到指定的位置
     *
     * @param downloadUrl   下载地址
     * @param localFilePath 下载完成保存在本地的路径文件名
     */
    public static void downloadFile(String downloadUrl, String localFilePath) {
        Downloader.Task downloadTask = new Downloader.Task()
                .setDownloadUrl(downloadUrl)
                .setDownloadFilePath(localFilePath)
                .setNotificationVisibility(Downloader.NOTIFICATION_HIDDEN)
                .setDownloadCallback(new InvalidDownloadCallback());
        Downloader.getInstance(MyApp.getApplication()).download(downloadTask);
    }


    private static class InvalidDownloadCallback implements IDownloadCallback {
        @Override
        public void onStart(Downloader.Task task) {
            Logg.i(TAG, "onStart: id=" + task.getDownloadId());
        }

        @Override
        public void onFailure(Downloader.Task task, Exception e) {
            Logg.i(TAG, "onFailure: id=" + task.getDownloadId() + " ,e:" + (e == null ? "null" : e.getMessage()));
        }

        @Override
        public void onSuccess(File file) {
            Logg.i(TAG, "onSuccess: filePath=" + file.getAbsolutePath());
        }

        @Override
        public void onProgress(long downloadedSize, long totalSize, String percent) {
            Logg.i("download progress update : downloadedSize=" + downloadedSize + " ,totalSize=" + totalSize + " ,percent=" + percent);
        }
    }
}
