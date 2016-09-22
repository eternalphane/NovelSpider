package com.github.novelspider.util;

import android.content.Context;
import android.os.Environment;

import com.github.novelspider.model.novel.Chapter;
import com.github.novelspider.model.novel.Novel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by EternalPhane on 2016/9/22.
 */

public class StorageUtil {
    private StorageUtil() {
    }

    public static void storeNovel(Novel novel) {
        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(getNovelFile(novel)));
            JSONArray jsonData = new JSONArray();
            for (Chapter c : novel.getContent()) {
                jsonData.put(c.getJsonObject());
            }
            dos.writeBytes(jsonData.toString());
            dos.close();
        } catch (IOException e) {
            // TODO: Add Exception Handler.
        }
    }

    public static void restoreNovel(Novel novel) {
        try {
            File file = getNovelFile(novel);
            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            byte[] fileData = new byte[(int) file.length()];
            dis.readFully(fileData);
            dis.close();
            JSONArray jsonData = new JSONArray(fileData.toString());
            novel.clearContent();
            for (int i = 0; i < jsonData.length(); i++) {
                JSONObject obj = jsonData.getJSONObject(i);
                novel.addChapter(new Chapter(obj.getString("name"), obj.getString("url")));
            }
        } catch (IOException e) {
            // TODO: Add Exception Handler.
        } catch (JSONException e) {
            // TODO: Add Exception Handler.
        }
    }

    public static void removeNovel(Novel novel) {
        getNovelFile(novel).delete();
        getNovelDir(novel).delete();
    }

    public static File getDefaultStorage(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        }
        return context.getFilesDir();
    }

    private static File getNovelDir(Novel novel) {
        File novelDir = new File(SettingsUtil.getValue("storage_location"), novel.getTitle());
        return (novelDir.exists() || novelDir.mkdirs()) ? novelDir : null;
    }

    private static File getNovelFile(Novel novel) {
        File novelDir = getNovelDir(novel);
        return novelDir == null ? null : new File(novelDir, novel.getTitle() + ".txt");
    }

    private static String md5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return new String(md.digest(text.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            // TODO: Add Exception Handler.
            return text;
        }
    }
}
