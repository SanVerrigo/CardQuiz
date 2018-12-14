package com.verrigo.cardquiz.utils;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.lang.reflect.Field;

public class UiUtils {
    public static TextView getTitleTextView(ActionBar toolbar) {
        Field toolbarTitleField;
        try {
            toolbarTitleField = Toolbar.class.getDeclaredField("mTitleTextView");
            toolbarTitleField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        try {
            return (TextView) toolbarTitleField.get(toolbar);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
