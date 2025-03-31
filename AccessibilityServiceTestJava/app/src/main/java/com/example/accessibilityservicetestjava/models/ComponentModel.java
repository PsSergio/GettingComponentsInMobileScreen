package com.example.accessibilityservicetestjava.models;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

public class ComponentModel {

    private final String className;

    private final String contentDescription;

    private final String text;

    private final boolean isClickable;

    private final boolean isFocusable;

    private final Rect bounds;


    public ComponentModel(String className, String contentDescription, String text, boolean isClickable, boolean isFocusable, Rect bounds) {
        this.className = className;
        this.contentDescription = contentDescription;
        this.text = text;
        this.isClickable = isClickable;
        this.isFocusable = isFocusable;
        this.bounds = bounds;
    }
}
