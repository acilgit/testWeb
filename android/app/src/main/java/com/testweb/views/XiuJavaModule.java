package com.testweb.views;

import android.app.Activity;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by XY on 2016/5/21.
 */
public class XiuJavaModule extends ReactContextBaseJavaModule{

    private Promise mPickerPromise;

    public static final String REACT_CLASS = "XModule";
    public static final int REQUEST_CODE_CHOOSE_FILE = 1001;

    final ReactApplicationContext reactContext;

    public XiuJavaModule(ReactApplicationContext reactContext){
        super(reactContext);
        this.reactContext = reactContext;
//        reactContext.addActivityEventListener(this);
        Log.e("ReactTag", " created  " + 4);
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        return constants;
    }
    public String getName(){
        return REACT_CLASS;
    }

    public Activity getActivity(){
        return getCurrentActivity();
    }
    public ReactApplicationContext getReactContext(){
        return reactContext;
    }

    @ReactMethod
    public void receivedMessageFromWebView(String message, Promise promise) {
        try {
            WritableMap map = Arguments.createMap();
//            map.
            promise.resolve(map);
        } catch (Exception e) {
            promise.reject(e);
        }
    }

    /*
        @ReactMethod
        public void chooseFile() {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image*//*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        getCurrentActivity().startActivityForResult(intent, REQUEST_CODE_CHOOSE_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String s = "intent:";
        if (requestCode == REQUEST_CODE_CHOOSE_FILE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
//                    if (mFileUploadCallbackFirst != null) {
//                        mFileUploadCallbackFirst.onReceiveValue(intent.getData());
//                        mFileUploadCallbackFirst = null;
//                    } else if (mFileUploadCallbackSecond != null) {
                    Uri[] dataUris;
                    try {
                        dataUris = new Uri[]{Uri.parse(data.getDataString())};
                    } catch (Exception e) {
                        dataUris = null;
                    }
                    s = data.getDataString();

//                        mFileUploadCallbackSecond.onReceiveValue(dataUris);
//                        mFileUploadCallbackSecond = null;
//                }
                }
            } else {
//                if (mFileUploadCallbackFirst != null) {
//                    mFileUploadCallbackFirst.onReceiveValue(null);
//                    mFileUploadCallbackFirst = null;
//                } else if (mFileUploadCallbackSecond != null) {
//                    mFileUploadCallbackSecond.onReceiveValue(null);
//                    mFileUploadCallbackSecond = null;
//                }
            }
            Toast.makeText(reactContext, "data:" + s, Toast.LENGTH_SHORT).show();
        }
    }*/
    public class JsEvents {

        public static final String EVENT_NAME_ReceivedMessageFromWebView = "messageFromWebView";
        public static final String EVENT_NAME_ReceivedMessageFromWebView_param = "message";
    }
}
