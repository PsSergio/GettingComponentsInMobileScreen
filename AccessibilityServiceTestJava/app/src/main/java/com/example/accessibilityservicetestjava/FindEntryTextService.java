package com.example.accessibilityservicetestjava;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.EditText;

public class FindEntryTextService extends AccessibilityService {

    private static final String TAG = "FindEntryTextService";
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        Log.e(TAG, "onAccessibilityEvent: ");

        if(!EditText.class.getName().equals(String.valueOf(event.getClassName()))) return;

        Log.e(TAG, "Found");
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        var info = new AccessibilityServiceInfo();

        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED | AccessibilityEvent.TYPE_VIEW_FOCUSED;

        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;

        info.notificationTimeout = 100;

        this.setServiceInfo(info);
    }
}
