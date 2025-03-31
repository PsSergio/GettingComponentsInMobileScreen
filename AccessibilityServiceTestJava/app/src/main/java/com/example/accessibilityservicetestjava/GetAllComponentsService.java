package com.example.accessibilityservicetestjava;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;
import android.widget.ImageView;

import com.example.accessibilityservicetestjava.models.ComponentModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GetAllComponentsService extends AccessibilityService {

    private static final String TAG = "ShowNodesInfo";
    private static final String TAG1 = "NodesLog";

    public String componentToJson(ComponentModel model){
        Gson gson = new Gson();
        return gson.toJson(model);
    }


    public void showComponents(AccessibilityNodeInfo rootNodes){
        Rect rect = new Rect();
        rootNodes.getBoundsInScreen(rect);

            var className = rootNodes.getClassName().toString();

            var contentDesc = "null";
            var text = "null";
            try{
                contentDesc = rootNodes.getContentDescription().toString();
            }catch (Exception e){
                contentDesc = "null";
            }
            try{
                text = rootNodes.getText().toString();
            }catch (Exception e){
                text = "null";
            }

            var isClickable = rootNodes.isClickable();
            var isFocusable = rootNodes.isFocusable();
            var componentModel = new ComponentModel(className, contentDesc, text, isClickable, isFocusable, rect);

            Log.e(TAG, componentToJson(componentModel));

    }

    public boolean isComponentValidLayout(AccessibilityNodeInfo node){
        if((Objects.equals(node.getClassName(), "android.view.View") ||
                Objects.equals(node.getClassName(), "android.widget.RelativeLayout") ||
                Objects.equals(node.getClassName(), "android.widget.LinearLayout") ||
                Objects.equals(node.getClassName(), "android.widget.FrameLayout") ||
                Objects.equals(node.getClassName(), "android.widget.ScrollView") ||
                Objects.equals(node.getClassName(), "android.widget.HorizontalScrollView") ||
                Objects.equals(node.getClassName(), "android.widget.ListView") ||
                Objects.equals(node.getClassName(), "android.widget.GridView") ||
                Objects.equals(node.getClassName(), "android.view.ViewGroup"))
                && (node.getContentDescription() != null || node.getText() != null) && (node.isClickable() || node.isFocusable())
        ) return true;

        return false;
    }

    public boolean filterComponents(AccessibilityNodeInfo node){
        if(!isComponentValidLayout(node)) return false;

        if(!node.isVisibleToUser()) return false;

        if(node.getClassName().toString().equals("android.widget.ImageView") && node.getContentDescription() == null)
            return false;

        if(node.getClassName().toString().equals("android.widget.TextView") && node.getText() == null)
            return false;

        return true;
    }



    public void getComponent(AccessibilityNodeInfo node){
        if(node == null) return;

        if(filterComponents(node)) {
            showComponents(node);
        }


        for(int i = 0; i < node.getChildCount(); i++){
//            if(isComponentValidLayout(node)) continue;
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
