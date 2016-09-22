package com.github.novelspider.model.novel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by EternalPhane on 2016/9/19.
 */
public class Novel {
    private String mTitle;
    private int mSiteId;
    private String mContentUrl;
    private List<Chapter> mContent;

    public Novel(String title, int siteId) {
        mTitle = title;
        mSiteId = siteId;
        mContent = new ArrayList<>();
    }

    public Novel(String title, int siteId, String url) {
        this(title, siteId);
        mContentUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getSiteId() {
        return mSiteId;
    }

    public String getContentUrl() {
        return mContentUrl;
    }

    public List<Chapter> getContent() {
        return Collections.unmodifiableList(mContent);
    }

    public Novel setTitle(String title) {
        mTitle = title;
        return this;
    }

    public Novel setSiteId(int siteId) {
        mSiteId = siteId;
        return this;
    }

    public Novel setContentUrl(String url) {
        mContentUrl = url;
        return this;
    }

    public Novel addChapter(Chapter chapter) {
        mContent.add(chapter);
        return this;
    }

    public Novel clearContent() {
        mContent.clear();
        return this;
    }
}
