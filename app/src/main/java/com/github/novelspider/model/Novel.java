package com.github.novelspider.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EternalPhane on 2016/9/19.
 */
public class Novel {
    private String mTitle;
    private String mSite;
    private List<Chapter> mContent;

    public Novel() {
        mContent = new ArrayList<>();
    }

    public Novel(String title) {
        this();
        mTitle = title;
    }

    public Novel(String title, String site) {
        this(title);
        mSite = site;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSite() {
        return mSite;
    }

    public Novel setTitle(String title) {
        mTitle = title;
        return this;
    }

    public Novel setSite(String site) {
        mSite = site;
        return this;
    }
}
