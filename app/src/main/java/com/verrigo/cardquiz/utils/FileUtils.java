package com.verrigo.cardquiz.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by repitch on 11.02.17.
 */
public class FileUtils {

    private FileUtils() {
        // empty
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException var2) {
        }
    }

}