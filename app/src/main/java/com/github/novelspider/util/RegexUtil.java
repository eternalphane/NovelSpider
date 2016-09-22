package com.github.novelspider.util;

/**
 * Created by EternalPhane on 2016/9/19.
 */
public class RegexUtil {
    private static final String REG_CONTENT = ".*?(目录|阅读|章节).*?";
    private static final String REG_CHAPTER = "[第]?[序〇零一二三四五六七八九十百千0-9]+[章节. ].*?";
    private static String sRegContent;
    private static String sRegChapter;

    static {
        initRegContent();
        initRegChapter();
    }

    private RegexUtil() {
    }

    public static synchronized String getRegContent() {
        return sRegContent;
    }

    public static synchronized String getRegChapter() {
        return sRegChapter;
    }

    public static synchronized void setRegContent(String r) {
        sRegContent = r;
    }

    public static synchronized void setRegChapter(String r) {
        sRegChapter = r;
    }

    public static synchronized void initRegContent() {
        sRegContent = REG_CONTENT;
    }

    public static synchronized void initRegChapter() {
        sRegChapter = REG_CHAPTER;
    }
}
