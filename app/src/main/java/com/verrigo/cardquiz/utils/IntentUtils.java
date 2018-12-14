package com.verrigo.cardquiz.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import com.verrigo.cardquiz.BuildConfig;

import java.io.File;

public class IntentUtils {
    private static final String SHARE_EMAIL = "epicmousepv@gmail.com";
    private static final String HTTP_SCHEME = "http://";
    private static final String HTTPS_SCHEME = "https://";

    public static Intent createShareCardset(Context context, @NonNull File outputFile, @NonNull String subject) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/json");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{SHARE_EMAIL});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        Uri outputUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", outputFile);
        intent.putExtra(Intent.EXTRA_STREAM, outputUri);
        return intent;
    }

}
