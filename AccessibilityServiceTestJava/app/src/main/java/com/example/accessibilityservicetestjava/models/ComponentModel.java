package com.example.accessibilityservicetestjava.models;

import android.view.accessibility.AccessibilityNodeInfo;

public class ComponentModel {

    private final AccessibilityNodeInfo node;

    private final boolean hasChild;

    public ComponentModel(AccessibilityNodeInfo node, boolean hasChild) {
        this.node = node;
        this.hasChild = hasChild;
    }

    public AccessibilityNodeInfo getNode() {
        return node;
    }
}
