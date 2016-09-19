package com.github.novelspider.util;

import java.util.regex.Pattern;

/**
 * Created by EternalPhane on 2016/9/19.
 */
public class RegexHelper {
    private static final String REG_CONTENT = ".*?(目录|阅读|章节).*?";
    private static final String REG_CHAPTER = "[第]?[序〇零一二三四五六七八九十百千0-9]+[章节. ].*?";
    private static String sRegContent;
    private static String sRegChapter;

    static {
        initRegContent();
        initRegChapter();
    }

    private RegexHelper() {
    }

    public static void initRegContent() {
        sRegContent = REG_CONTENT;
    }

    public static void initRegChapter() {
        sRegChapter = REG_CHAPTER;
    }

    public static String getRegContent() {
        return sRegContent;
    }

    public static String getRegChapter() {
        return sRegChapter;
    }

    public static void setRegContent(String r) {
        sRegContent = r;
    }

    public static void setRegChapter(String r) {
        sRegChapter = r;
    }
}
