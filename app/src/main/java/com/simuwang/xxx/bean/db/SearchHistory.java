package com.simuwang.xxx.bean.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * function:
 *
 * <p>
 * Created by Leo on 2017/11/14 .
 */
@Entity(nameInDb = "search_history")
public class SearchHistory {
    @Id(autoincrement = true)
    private Long   id;
    private String searchId;
    private String searchName;
    private String searchType;

    @Generated(hash = 409526961)
    public SearchHistory(Long id, String searchId, String searchName,
                         String searchType) {
        this.id = id;
        this.searchId = searchId;
        this.searchName = searchName;
        this.searchType = searchType;
    }

    public SearchHistory(String searchId, String searchName,
                         String searchType) {
        this.searchId = searchId;
        this.searchName = searchName;
        this.searchType = searchType;
    }

    @Generated(hash = 1905904755)
    public SearchHistory() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSearchId() {
        return this.searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public String getSearchName() {
        return this.searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchType() {
        return this.searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

}
