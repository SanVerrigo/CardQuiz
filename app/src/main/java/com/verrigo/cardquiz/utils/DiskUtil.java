package com.verrigo.cardquiz.utils;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.xml.sax.ErrorHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.content.ContentResolver.SCHEME_CONTENT;

public class DiskUtil {
    private static final String JSON_EXT = ".json";

    public static File saveJsonOnDisk(Context context, final String jsonStr, String fileName) {
        File outputFile = null;
        FileOutputStream fOut = null;
        OutputStreamWriter myOutWriter = null;
        if (!fileName.endsWith(JSON_EXT)) {
            fileName = fileName + JSON_EXT;
        }
        try {
            final File cacheDir = context.getExternalCacheDir();
            cacheDir.mkdirs();
            outputFile = new File(cacheDir, fileName);
            if (outputFile.exists()) {
                outputFile.delete();
            }
            fOut = new FileOutputStream(outputFile);
            myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(jsonStr);
            return outputFile;
        } catch (IOException e) {
//            Timber.e(e);
        } finally {
            FileUtils.closeQuietly(myOutWriter);
            FileUtils.closeQuietly(fOut);
        }
        return outputFile;
    }


    @Nullable
    public static String readStringFromUri(Context context, @Nullable Uri uri) {
        if (uri == null) {
            return null;
        }
        InputStream is = null;
        try {
            if (TextUtils.equals(SCHEME_CONTENT, uri.getScheme())) {
                is = context.getContentResolver().openInputStream(uri);
            } else {
                is = new FileInputStream(uri.getPath());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                total.append(line).append('\n');
            }
            return total.toString();
        } catch (IOException e) {
            return null;
        } finally {
            FileUtils.closeQuietly(is);
        }
    }

}
