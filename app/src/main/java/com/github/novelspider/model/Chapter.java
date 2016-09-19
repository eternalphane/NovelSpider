package com.github.novelspider.model;

/**
 * Created by EternalPhane on 2016/9/19.
 */
public class Chapter {
    private String mName;
    private String mUrl;

    public String getName() {
        return mName;
    }

    public String getUrl() {
        return mUrl;
    }

    public Chapter setName(String name) {
        mName = name;
        return this;
    }

    public Chapter setUrl(String url) {
        mUrl = url;
        return this;
    }
}
