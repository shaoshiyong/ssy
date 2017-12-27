package com.simuwang.xxx.manager.db;

import android.database.sqlite.SQLiteDatabase;

import com.simuwang.xxx.MyApp;
import com.simuwang.xxx.bean.db.DaoMaster;
import com.simuwang.xxx.bean.db.DaoSession;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * function:DaoSession管理中心，主要为外部提供DaoSession以获取对应表的Dao从而封装对应的实现类
 *
 * <p>
 * Created by Leo on 2017/3/13.
 */
@SuppressWarnings("ALL")
public class DaoSessionCenter {
    private static final String DB_NAME_CORE = "app_core.db";

    private static volatile AtomicReference<DaoSessionCenter> INSTANCE = new AtomicReference<>();

    private static Map<String, DaoSession> mDaoSeesionMap;
    private static DaoSession              mDaoSession;

    private DaoSessionCenter() {
        mDaoSeesionMap = new HashMap<>();
    }

    private static DaoSessionCenter getInstance() {
        for (; ; ) {
            DaoSessionCenter manager = INSTANCE.get();
            if (manager != null) return manager;
            manager = new DaoSessionCenter();
            if (INSTANCE.compareAndSet(null, manager)) return manager;
        }
    }

    /**
     * 获取daoSession
     */
    public static DaoSession daoSession() {
        return daoSession(DB_NAME_CORE);
    }

    /**
     * 获取daoSession
     *
     * @param dbName 指定数据库名称
     */
    public static DaoSession daoSession(String dbName) {
        if (mDaoSeesionMap != null && mDaoSeesionMap.get(dbName) != null) {
            return mDaoSeesionMap.get(dbName);
        }
        return getInstance().getDaoSession(dbName);
    }

    private DaoSession getDaoSession(String dbName) {
        DaoMaster.DevOpenHelper helper     = new DaoMaster.DevOpenHelper(MyApp.getApplication(), dbName, null);
        SQLiteDatabase          db         = helper.getWritableDatabase();
        DaoMaster               daoMaster  = new DaoMaster(db);
        DaoSession              daoSession = daoMaster.newSession();
        mDaoSeesionMap.put(dbName, daoSession);
        return daoSession;
    }
}
