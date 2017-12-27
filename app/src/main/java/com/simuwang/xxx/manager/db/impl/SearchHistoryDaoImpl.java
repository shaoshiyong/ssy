package com.simuwang.xxx.manager.db.impl;


import android.text.TextUtils;

import com.simuwang.xxx.bean.db.SearchHistory;
import com.simuwang.xxx.bean.db.SearchHistoryDao;
import com.simuwang.xxx.manager.db.DaoSessionCenter;

import java.util.List;

/**
 * function: 搜索历史记录
 *
 * <p>
 * Created by Leo on 2017/11/14.
 */

public class SearchHistoryDaoImpl {

    private final static int HISTORY_PAGE_COUNT = 10;
    private final static int HISTORY_MAX_COUNT  = 30;

    /**
     * 插入一条记录
     * 插入前判断是否存在该条记录；存在：删除重新插入（让排序靠前）；
     * 不存在：判断当前记录已经达到最大数30条，是：删除最早的一条；否：插入数据
     */
    public static void insert(SearchHistory searchHistory) {
        if (searchHistory == null || TextUtils.isEmpty(searchHistory.getSearchId())) return;
        SearchHistoryDao    dao      = DaoSessionCenter.daoSession().getSearchHistoryDao();
        String              searchId = searchHistory.getSearchId();
        List<SearchHistory> dates    = queryBySearId(searchId);
        // 判断是否已经存在记录
        if (dates == null || dates.size() == 0) {
            // 判断是否该类别记录已经达到最大数
            dates = queryAllHistory();
            if (dates != null && dates.size() >= HISTORY_MAX_COUNT) {
                for (int i = HISTORY_MAX_COUNT + 1; i < dates.size(); i++) {
                    dao.delete(dates.get(i));
                }
            }
            dao.insert(searchHistory);
        } else {
            // 更新记录
            dao.delete(dates.get(0));
            dao.insert(searchHistory);
        }
    }

    /**
     * 根据类别清除历史数据
     */
    public static void deleteHistoryById(String searchId) {
        DaoSessionCenter.daoSession().getSearchHistoryDao().deleteInTx(queryBySearId(searchId));
    }

    /**
     * 分页查询历史
     */
    public static List<SearchHistory> queryHistory(int page) {
        return DaoSessionCenter.daoSession().getSearchHistoryDao().queryBuilder()
                .offset(page * HISTORY_PAGE_COUNT).limit(HISTORY_PAGE_COUNT).orderDesc(SearchHistoryDao.Properties.Id).list();
    }

    /**
     * 查询所有记录
     */
    private static List<SearchHistory> queryAllHistory() {
        return DaoSessionCenter.daoSession().getSearchHistoryDao()
                .queryBuilder().orderDesc(SearchHistoryDao.Properties.Id).orderAsc(SearchHistoryDao.Properties.Id).list();
    }

    /**
     * 根据搜索ID查询记录
     */
    private static List<SearchHistory> queryBySearId(String searchId) {
        return DaoSessionCenter.daoSession().getSearchHistoryDao()
                .queryBuilder().where(SearchHistoryDao.Properties.SearchId.eq(searchId)).list();
    }

    public static void clearAll() {
        DaoSessionCenter.daoSession().getSearchHistoryDao().deleteAll();
    }

}
