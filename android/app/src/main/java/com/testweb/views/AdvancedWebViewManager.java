package com.testweb.views;

/**
 * Created by XY on 2016/5/21.
 */
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.views.webview.ReactWebViewManager;
import com.facebook.react.views.webview.WebViewConfig;

import java.util.Map;

import javax.annotation.Nullable;

public class AdvancedWebViewManager extends ReactWebViewManager {

    private static final String REACT_CLASS = "RCTXAdvancedWebView";
//    private static final String REACT_CLASS = "RCTWebViewBridge";

    public static final int COMMAND_INJECT_BRIDGE_SCRIPT = 100;
    public static final int COMMAND_SEND_TO_BRIDGE = 101;

    private boolean initializedBridge;

    public AdvancedWebViewManager() {
        super();
        initializedBridge = false;
    }

    public AdvancedWebViewManager(WebViewConfig webViewConfig) {
        super(webViewConfig);
        initializedBridge = false;
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public @Nullable Map<String, Integer> getCommandsMap() {
        Map<String, Integer> commandsMap = super.getCommandsMap();

        commandsMap.put("injectBridgeScript", COMMAND_INJECT_BRIDGE_SCRIPT);
        commandsMap.put("sendToBridge", COMMAND_SEND_TO_BRIDGE);

        return commandsMap;
    }

    @Override
    public void receiveCommand(WebView root, int commandId, @Nullable ReadableArray args) {
        Log.e("ReactTag", " receiveCommand  ");
        super.receiveCommand(root, commandId, args);

        switch (commandId) {
            case COMMAND_INJECT_BRIDGE_SCRIPT:
                injectBridgeScript(root);
                break;
            case COMMAND_SEND_TO_BRIDGE:
                sendToBridge(root, args.getString(0));
                break;
            default:
                //do nothing!!!!
        }
    }

    private void sendToBridge(WebView root, String message) {
        //root.loadUrl("javascript:(function() {\n" + script + ";\n})();");
        String script = "WebViewBridge.onMessage('" + message + "');";
        AdvancedWebViewManager.evaluateJavascript(root, script);

        Log.e("ReactTag", " sendToBridge  " + message);
    }

    private void injectBridgeScript(WebView root) {
        Log.e("ReactTag", " injectBridgeScript  ");
        //this code needs to be called once per context
        if (!initializedBridge) {
            root.addJavascriptInterface(new JavascriptBridge((ReactContext) root.getContext()), "WebViewBridgeAndroid");
            initializedBridge = true;
            root.reload();
        }

        // this code needs to be executed everytime a url changes.
        AdvancedWebViewManager.evaluateJavascript(root, ""
                + "(function() {"
                + "if (window.WebViewBridge) return;"
                + "var customEvent = document.createEvent('Event');"
                + "var WebViewBridge = {"
                + "send: function(message) { WebViewBridgeAndroid.send(message); },"
                + "onMessage: function() {}"
                + "};"
                + "window.WebViewBridge = WebViewBridge;"
                + "customEvent.initEvent('WebViewBridge', true, true);"
                + "document.dispatchEvent(customEvent);"
                + "}());");
    }

    static private void evaluateJavascript(WebView root, String javascript) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            root.evaluateJavascript(javascript, null);
        } else {
            root.loadUrl("javascript:" + javascript);
        }
    }

    class JavascriptBridge {
        private ReactContext context;

        public JavascriptBridge(ReactContext context) {
            this.context = context;

        }

        @JavascriptInterface
        public void send(String message) {
            WritableMap params = Arguments.createMap();
            params.putString("message", message);
            context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("webViewBridgeMessage", params);
        }
    }
}