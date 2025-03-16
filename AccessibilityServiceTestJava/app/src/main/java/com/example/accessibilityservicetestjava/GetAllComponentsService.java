package com.example.accessibilityservicetestjava;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import com.example.accessibilityservicetestjava.models.ComponentModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GetAllComponentsService extends AccessibilityService {

    private static final String TAG = "ShowNodesInfo";
    private static final String TAG1 = "NodesLog";


    public void showComponents(AccessibilityNodeInfo rootNodes){
//        for(int i = 0; i < rootNodes.size(); i++){
            Log.e(TAG, rootNodes.getClassName().toString());
            try{
                Log.e(TAG, rootNodes.getContentDescription().toString());
            }catch (Exception e){
                Log.e(TAG, "null");
            }
            try{
                Log.e(TAG, rootNodes.getText().toString());
            }catch (Exception e){
                Log.e(TAG, "null");
            }

            Log.e(TAG, "-------------------------------");
//        }
    }

    public boolean isComponentLayout(String className){
        if(Objects.equals(className, "android.view.View") ||
                Objects.equals(className, "android.widget.RelativeLayout") ||
                Objects.equals(className, "android.widget.LinearLayout") ||
                Objects.equals(className, "android.widget.FrameLayout") ||
                Objects.equals(className, "android.widget.ScrollView") ||
                Objects.equals(className, "android.widget.HorizontalScrollView") ||
                Objects.equals(className, "android.widget.ListView") ||
                Objects.equals(className, "android.widget.GridView") ||
                Objects.equals(className, "android.view.ViewGroup")
        ) return true;

        return false;
    }

    public void getComponent(AccessibilityNodeInfo node){
        if(node == null) return;

        if(!isComponentLayout(node.getClassName().toString())) {
            showComponents(node);
        }


        for(int i = 0; i < node.getChildCount(); i++){
            getComponent(node.getChild(i));
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.e(TAG, "\n\nonAccessibilityEvent: \n");

        AccessibilityNodeInfo rootNode = getRootInActiveWindow();

        if(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {

            getComponent(rootNode);

            Log.e(TAG, "----------------------- VIEW ENDED -----------------------------");

        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        var info = new AccessibilityServiceInfo();

//        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED | AccessibilityEvent.TYPE_VIEW_FOCUSED;

        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED | AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;

        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;

        info.notificationTimeout = 100;

        this.setServiceInfo(info);
    }
}
