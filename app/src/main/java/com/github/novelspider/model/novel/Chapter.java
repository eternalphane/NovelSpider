package com.github.novelspider.model.novel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by EternalPhane on 2016/9/19.
 */
public class Chapter {
    private String mName;
    private String mUrl;

    public Chapter(String name, String url) {
        mName = name;
        mUrl = url;
    }

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

    public JSONObject getJsonObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", mName).put("url", mUrl);
            return obj;
        } catch (JSONException e) {
            // TODO: Add Exception Handler.
            return null;
        }
    }
}
