package com.github.novelspider.util;

import com.github.novelspider.model.Novel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * Created by EternalPhane on 2016/9/19.
 */
public class NovelGenerator {
    private static NovelGenerator sInstance = new NovelGenerator();
    private static int sMaxDepth = 3;
    private Novel mNovelBuf;
    private Set<String> mVisited;
    private Stack<String> mTarget;
    private Queue<String> mTargetBuf;
    private int mDepth;

    private NovelGenerator() {
        mVisited = new HashSet<>();
        mTarget = new Stack<>();
        mTargetBuf = new LinkedList<>();
        mDepth = 0;
    }

    public static NovelGenerator getInstance() {
        return sInstance;
    }

    public Novel generate(String title, String site) {
        mNovelBuf = new Novel(title, site);
        return searchForContent() ? mNovelBuf : null;
    }

    public boolean regenerate(Novel novel, String site) {
        mNovelBuf = novel;
        novel.setSite(site);
        return searchForContent();
    }

    private boolean searchForContent() {
        try {
            String url = "http://www.baidu.com/s?wd=" + mNovelBuf.getTitle();
            Document doc = Jsoup.connect(url).get();
            mVisited.add(doc.baseUri());
            if (crawl(doc.select("a[data-click]:has(em)"))) {
                return true;
            }
            while (mTarget.size() > 0) {
                url = mTarget.pop();
                if (url == "") {
                    mDepth--;
                    continue;
                }
                try {
                    if (crawl(Jsoup.connect(url).get().select("a"))) {
                        return true;
                    }
                } catch (IOException e) {
                    // TODO: Add Exception Handler.
                    continue;
                }
            }
            return false;
        } catch (IOException e) {
            // TODO: Add Exception Handler.
            return false;
        }
    }

    private boolean crawl(Elements links) {
        for (Element link : links) {
            String url = link.attr("abs:href");
            if (checkContent(url)) {
                return true;
            }
            if (!mVisited.contains(url) && mDepth < sMaxDepth) {
                mTargetBuf.add(url);
                mVisited.add(url);
            }
        }
        mTarget.push("");
        mTarget.addAll(mTargetBuf);
        mTargetBuf.clear();
        mDepth++;
        return false;
    }

    private boolean checkContent(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            if (doc.select(String.format("a:matches(%s)", RegexHelper.getRegChapter())).size() > 10) {
                generateContent(doc);
                return true;
            }
            return false;
        } catch (IOException e) {
            // TODO: Add Exception Handler.
            return false;
        }
    }

    private void generateContent(Document doc) {
    }
}
