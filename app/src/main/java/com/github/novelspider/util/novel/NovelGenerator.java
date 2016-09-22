package com.github.novelspider.util.novel;

import android.database.Cursor;

import com.github.novelspider.model.novel.Chapter;
import com.github.novelspider.model.novel.Novel;
import com.github.novelspider.model.database.DbContract;
import com.github.novelspider.util.DbManager;
import com.github.novelspider.util.RegexUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * Created by EternalPhane on 2016/9/19.
 */
public class NovelGenerator {
    private static NovelGenerator sInstance = new NovelGenerator();
    private List<String> mSites;
    private DbManager mDbMgr;
    private Novel mNovelBuf;
    private Set<String> mVisited;
    private Stack<String> mTarget;
    private Queue<String> mTargetBuf;
    private int mMaxDepth = 3;
    private int mDepth;

    private NovelGenerator() {
        mSites = new ArrayList<>();
        mDbMgr = DbManager.getInstance();
        mVisited = new HashSet<>();
        mTarget = new Stack<>();
        mTargetBuf = new LinkedList<>();
        mDepth = 0;
        loadSites();
    }

    public static synchronized NovelGenerator getInstance() {
        return sInstance;
    }

    public synchronized List<String> getSites() {
        return Collections.unmodifiableList(mSites);
    }

    public synchronized Novel generateNovel(String title, int siteId) {
        mNovelBuf = new Novel(title, siteId);
        return searchForContent() ? mNovelBuf : null;
    }

    public synchronized boolean regenerateNovel(Novel novel, int siteId) {
        mNovelBuf = novel;
        mNovelBuf.setSiteId(siteId);
        return searchForContent();
    }

    public synchronized String generateChapter(Novel novel, int chapterId) {
        try {
            Document doc = Jsoup.connect(novel.getContent().get(chapterId).getUrl()).get();
            Elements links = doc.select(":not(:has(a, script), a, script, head *)");
            Element body = doc.body();
            Elements e = body.select(":not(:has(:has(a))):has(a)");
            while (!e.isEmpty()) {
                e.remove();
                e = body.select(":not(:has(:has(a))):has(a)");
            }
            body.select(String.format(
                    "a, script, head, foot, :matchesOwn(%s)",
                    RegexUtil.getRegChapter()
            )).remove();
            String chapter = Parser.unescapeEntities(
                    Jsoup.clean(
                            Jsoup.clean(
                                    body.html(),
                                    "",
                                    Whitelist.none().addTags("br"),
                                    new Document.OutputSettings().prettyPrint(true)
                            ),
                            "",
                            Whitelist.none(),
                            new Document.OutputSettings().prettyPrint(false)
                    ),
                    false
            ).replace("\u00a0", "").replaceAll(" \\n|\\n |$ ", "\n").replace("\n\n", "\n");
            return chapter;
        } catch (IOException e) {
            // TODO: Add Exception Handler.
            return null;
        }
    }

    private void loadSites() {
        try (Cursor c = mDbMgr.openDb().query(
                DbContract.Sites.TABLE_NAME,
                new String[]{
                        DbContract.Sites._ID,
                        DbContract.Sites.COLUMN_NAME_SITE
                },
                null,
                null,
                null,
                null,
                DbContract.Sites._ID
        )) {
            while (c.moveToNext()) {
                mSites.add(c.getString(c.getColumnIndexOrThrow(DbContract.Sites.COLUMN_NAME_SITE)));
            }
        } catch (IllegalArgumentException e) {
            // TODO: Add Exception Handler.
        } finally {
            mDbMgr.closeDb();
        }
    }

    private synchronized boolean searchForContent() {
        try {
            String url = String.format(
                    "http://www.baidu.com/s?wd=%s site:%s",
                    mNovelBuf.getTitle(),
                    mSites.get(mNovelBuf.getSiteId())
            );
            Document doc = Jsoup.connect(url).get();
            mVisited.add(doc.baseUri());
            if (crawl(doc.select("a[data-click]:has(em)"))) {
                return true;
            }
            while (mTarget.size() > 0) {
                url = mTarget.pop();
                if (url.equals("")) {
                    mDepth--;
                    continue;
                }
                try {
                    if (crawl(Jsoup.connect(url).get().select("a"))) {
                        return true;
                    }
                } catch (IOException e) {
                    // TODO: Add Exception Handler.
                }
            }
            return false;
        } catch (IOException e) {
            // TODO: Add Exception Handler.
            return false;
        } finally {
            mVisited.clear();
        }
    }

    private boolean crawl(Elements links) {
        for (Element link : links) {
            String url = link.attr("abs:href");
            if (checkContent(url)) {
                return true;
            }
            if (!mVisited.contains(url) && mDepth < mMaxDepth) {
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
            Elements links = doc.select(String.format("a:matches(%s)", RegexUtil.getRegChapter()));
            if (links.size() > 10) {
                mNovelBuf.clearContent();
                for (Element link : links) {
                    mNovelBuf.addChapter(new Chapter(link.text(), link.attr("abs:href")));
                }
                return true;
            }
        } catch (IOException e) {
            // TODO: Add Exception Handler.
        }
        return false;
    }
}
