package com.testweb.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

/**
 * Created by 18953 on 2016/5/19.
 */
public class TextViewManager extends SimpleViewManager<TextView> {

    public static final String REACT_CLASS = "RCTTextView";
    public static final int REQUEST_CODE_CHOOSE_FILE = 1001;
    private ThemedReactContext reactContext;
    private Activity activity;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected TextView createViewInstance(ThemedReactContext reactContext) {
        this.reactContext = reactContext;
        TextView textView = new TextView(reactContext);
        activity = reactContext.getNativeModule(XiuJavaModule.class).getActivity();
        return textView;
    }

    @ReactProp(name = "text")
    public void setText(final TextView view, @Nullable final String text) {
        Log.e("ReactTag", "setText");
        view.setText(text);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
//                    activity.startActivityForResult(intent, 51426);
                    return true;
                } else {
                    return false;
                }


            }
        });
    }


    @ReactProp(name = "textSize")
    public void setTextSize(TextView view, float fontSize) {
        view.setTextSize(fontSize);
        Toast.makeText(reactContext, "fontSize:" + fontSize, Toast.LENGTH_SHORT).show();
    }

    @ReactProp(name = "textColor", defaultInt = Color.BLACK)
    public void setTextColor(TextView view, int textColor) {
        view.setTextColor(textColor);
    }

    @ReactProp(name = "isAlpha", defaultBoolean = false)
    public void setTextAlpha(TextView view, boolean isAlpha) {
        if (isAlpha) {
            view.setAlpha(0.5f);
        } else {
        }
    }

}